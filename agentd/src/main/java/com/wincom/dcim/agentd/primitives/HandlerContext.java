package com.wincom.dcim.agentd.primitives;

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
    public void send(Message m, Handler reply);

    /**
     * Called by the sender when a message is processed, the result maybe
     * success or not.
     *
     * @param m
     */
    public void onRequestCompleted(Message m);

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
     *
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
    public void setActive(boolean b);

    public void setInboundHandler(ChannelInboundHandler handler);

    public ChannelInboundHandler getInboundHandler();

    public void setOutboundHandler(ChannelOutboundHandler handler);

    public ChannelOutboundHandler getOutboundHandler();

    public void fireClosed(Message m);

    public static class Adapter implements HandlerContext {

        protected Logger log = LoggerFactory.getLogger(this.getClass());

        protected StateMachine machine;
        private final Map<Object, Object> variables;
        protected final ConcurrentLinkedQueue<Request> queue;
        protected Request current;
        private boolean active;
        protected ChannelOutboundHandler outboundHandler;
        protected ChannelInboundHandler inboundHandler;

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
        public ChannelInboundHandler getInboundHandler() {
            return this.inboundHandler;
        }

        @Override
        public void setInboundHandler(ChannelInboundHandler handler) {
            if(isActive()) {
                this.inboundHandler.handleChannelInactive(this, new ChannelInactive(this));
            }
            this.inboundHandler = handler;
            if(isActive()) {
                this.inboundHandler.handleChannelActive(this, new ChannelActive(this));
            }
        }

        @Override
        public void setOutboundHandler(ChannelOutboundHandler handler) {
            this.outboundHandler = handler;
        }

        @Override
        public ChannelOutboundHandler getOutboundHandler() {
            return this.outboundHandler;
        }

        @Override
        public void send(Message m) {
            send(m, new Handler.Default());
        }

        @Override
        public void send(Message m, Handler reply) {
            Request s = new Request(m, reply);
            if (m.isOob()) {
                sendImmediate(s);
            } else {
                enqueueForSendWhenActive(s);
            }
        }

        protected synchronized void sendImmediate(Request s) {
            current = s;
            current.message.apply(this, this.outboundHandler);
        }

        public synchronized void enqueueForSendWhenActive(Request s) {
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
            active = false;
            if (isInprogress()) {
                current.handler.handle(this, m);
                current = null;
            }

            while (!queue.isEmpty()) {
                Request s = queue.poll();
                s.handler.handle(this, m);
            }
        }

        @Override
        public synchronized void onRequestCompleted(Message response) {
            if (isInprogress()) {
                current.handler.handle(this, response);
                current = null;
            } else {
                inboundHandler.handle(this, response);
            }
            sendNext();
        }

        protected void sendNext() {
            //synchronized (queue)
            if (isActive()) {
                if (!queue.isEmpty()) {
                    current = queue.poll();
                    current.message.apply(this, this.outboundHandler);
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

        @Override
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
        public void setActive(boolean b) {
            this.active = b;
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
        public void send(Message m, Handler reply) {
            log.info(String.format("send(%s, %s)", m, reply));
        }

        @Override
        public void fire(Message m) {
            log.info(String.format("fire(%s)", m));
        }
    }
}
