package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.statemachine.SendMessageState;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import static java.lang.System.out;
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
     * Get state machine.
     *
     * @return
     */
    public StateMachine getStateMachine();

    public static abstract class Adapter implements HandlerContext {

        Logger log = LoggerFactory.getLogger(this.getClass());

        private StateMachine machine;
        private final Map<Object, Object> variables;
        protected final ConcurrentLinkedQueue<State> queue;
        private State current;

        public Adapter() {
            this(new StateMachine());
        }

        public Adapter(StateMachine machine) {
            this.machine = machine;
            this.variables = new HashMap<>();
            this.queue = new ConcurrentLinkedQueue<>();
            this.current = null;
        }

        @Override
        public void send(Message m) {
            State s = new SendMessageState(m, getHandler(m.getClass()));
            enqueueOrSend(s);
        }

        @Override
        public void send(Message m, HandlerContext reply) {
            State s = new SendMessageState(m, getHandler(m.getClass()), reply);
            enqueueOrSend(s);
        }

        private void enqueueOrSend(State s) {
            //synchronized (queue) 
            {
                if (isInprogress()) {
                    queue.add(s);
                } else {
                    current = s;
                    current.enter(this);
                }
            }
        }

        @Override
        public void fire(Message m) {
            out.println(m);
            machine.on(this, m);
        }

        @Override
        public void onSendComplete(Message response) {
            if (isInprogress()) {
                current.on(this, response);
                current = null;
            } else {
                log.warn("response ignored: " + response);
            }
            sendNext();
        }

        private void sendNext() {
            //synchronized (queue) 
            {
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

        @Override
        public StateMachine getStateMachine() {
            return machine;
        }

        public boolean isInprogress() {
            return this.current != null;
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
            log.debug("ignored: " + m);
        }
    }
}
