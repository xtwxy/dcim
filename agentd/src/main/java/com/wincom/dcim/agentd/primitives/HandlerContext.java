package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.statemachine.SendMessageState;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public interface HandlerContext {

    /**
     * Send message via the underlying service.
     *
     * @param m
     */
    public void send(Message m);

    /**
     * Send message via the underlying service, and reply to reply context.
     *
     * @param m
     * @param reply
     */
    public void send(Message m, HandlerContext reply);

    /**
     * Called by the sender when a message is processed, the result maybe
     * success or not.
     *
     * @param m
     */
    public void onSendComplete(Message m);

    /**
     * Initialize handlers when outbound is activated.
     *
     * @param outboundContext
     */
    public void initHandlers(HandlerContext outboundContext);

    /**
     * Get handler by class type of the message.
     *
     * @param clazz
     * @return
     */
    public Handler getHandler(Class clazz);

    /**
     * Fire message from the underlying service to the state machine.
     *
     * @param m
     */
    public void fire(Message m);

    /**
     * Set context variables.
     *
     * @param key
     * @param value
     */
    public void set(Object key, Object value);

    /**
     * Get context variables.
     *
     * @param key
     * @return
     */
    public Object get(Object key);
    
    /**
     * Remove context variables.
     * @param key
     * @return 
     */
    public Object remove(Object key);

    /**
     * Get state machine.
     *
     * @return
     */
    public StateMachine getStateMachine();

    /**
     * Test if channel is ready to send & receive.
     *
     * @return
     */
    public boolean isActive();

    /**
     * Called by the underlying service to update the ready state.
     *
     * @param active
     */
    public void setActive(boolean active);

    /**
     * Set inbound handler to this context.
     *
     * @param handler
     */
    public void setInboundHandler(Handler handler);

    /**
     * Get inbound handler.
     *
     * @return
     */
    public Handler getInboundHandler();

    public void fireClosed(Message m);
    public void close();

    public static abstract class Adapter implements HandlerContext {

        Logger log = LoggerFactory.getLogger(this.getClass());

        private StateMachine machine;
        private final Map<Object, Object> variables;
        protected final ConcurrentLinkedQueue<State> queue;
        private State current;
        private boolean active;
        protected Handler inboundHandler;

        public Adapter() {
            this(new StateMachine());
        }

        public Adapter(StateMachine machine) {
            this.machine = machine;
            this.variables = new HashMap<>();
            this.queue = new ConcurrentLinkedQueue<>();
            this.current = null;
            this.active = false;
        }

        @Override
        public Handler getInboundHandler() {
            return this.inboundHandler;
        }

        @Override
        public void setInboundHandler(Handler handler) {
            this.inboundHandler = handler;
        }

        @Override
        public void send(Message m) {
            State s = new SendMessageState(m);
            if (m.isOob()) {
                sendImmediate(s);
            } else {
                enqueueForSendWhenActive(s);
            }
        }

        @Override
        public void send(Message m, HandlerContext reply) {
            State s = new SendMessageState(m, reply);
            if (m.isOob()) {
                sendImmediate(s);
            } else {
                enqueueForSendWhenActive(s);
            }
        }

        private synchronized void sendImmediate(State s) {
            current = s;
            current.enter(this);
        }

        private synchronized void enqueueForSendWhenActive(State s) {
            queue.add(s);
            if (isActive()) {
                if (!isInprogress()) {
                    sendNext();
                } else {
                    // wait for complete
                }
            } else {
                // wait for activation
            }
        }

        @Override
        public void fire(Message m) {
            machine.on(this, m);
        }

        @Override
        public void fireClosed(Message m) {
            setActive(false);
            if (isInprogress()) {
                current.on(this, m);
                current = null;
            }

            while (!queue.isEmpty()) {
                State s = queue.poll();
                s.on(this, m);
            }
        }

        @Override
        public synchronized void onSendComplete(Message response) {
            if (isInprogress()) {
                current.on(this, response);
                current = null;
            } else {
                log.warn(String.format("onSendComplete(%s): response ignored. ", response));
            }
            sendNext();
        }

        private void sendNext() {
            //synchronized (queue)
            if (isActive()) {
                if (queue.isEmpty()) {
                    return;
                } else {
                    current = queue.poll();
                    current.enter(this);
                }
            }
        }

        @Override
        public void set(Object key, Object value) {
            variables.put(key, value);
        }

        @Override
        public Object get(Object key) {
            return variables.get(key);
        }

        public Object remove(Object key) {
            return variables.remove(key);
        }

        @Override
        public StateMachine getStateMachine() {
            return machine;
        }

        public boolean isInprogress() {
            return this.current != null;
        }

        @Override
        public boolean isActive() {
            return active;
        }

        @Override
        public void setActive(boolean active) {
            this.active = active;
            if (isActive() && !isInprogress()) {
                sendNext();
            }
        }

        private void printState(Message m) {
            if (current != null || !queue.isEmpty()) {
                log.info(m.toString());
                log.info(machine.toString());
                log.info(variables.toString());
                log.info(queue.toString());
                log.info("inprogress: " + current);
                log.info("active: " + active);
                log.info("inboundHandler: " + inboundHandler);
                new Exception().printStackTrace();
            }
        }
    }

    public static class NullContext extends Adapter {

        public NullContext() {
            super(null);
        }

        @Override
        public void send(Message m, HandlerContext reply) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Handler getHandler(Class clazz) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void fire(Message m) {
        }

        @Override
        public void close() {
            
        }
        
        @Override
        public void initHandlers(HandlerContext outboundContext) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
