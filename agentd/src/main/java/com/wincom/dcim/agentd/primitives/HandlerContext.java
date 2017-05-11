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
     * Called by the sender when a message is processed, the result maybe
     * success or not.
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

    /**
     * Set state machine.
     *
     * @param sm
     */
    public void setStateMachine(StateMachine sm);

    public static abstract class Adapter implements HandlerContext {

        Logger log = LoggerFactory.getLogger(this.getClass());

        private StateMachine machine;
        private final Map<Object, Object> variables;
        protected final ConcurrentLinkedQueue<Message> queue;
        private boolean inprogress;

        public Adapter(StateMachine machine) {
            this.machine = machine;
            this.variables = new HashMap<>();
            this.queue = new ConcurrentLinkedQueue<>();
            this.inprogress = false;
        }

        @Override
        public void send(Message m) {
//            synchronized (queue) 
            {
                if (!inprogress) {
                    inprogress = true;
                } else {
                    queue.add(m);
                    return;
                }
            }
            doSend(m);
        }

        protected void doSend(Message m) {
            m.apply(this, getHandler(m.getClass()));
        }

        @Override
        public void onSendComplete(Message ignore) {
            if (inprogress) {
                Message m = null;
//                synchronized (queue) 
                {
                    if (queue.isEmpty()) {
                        inprogress = false;
                        return;
                    } else {
                        m = queue.poll();
                    }
                }
                doSend(m);
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

        @Override
        public void setStateMachine(StateMachine sm) {
            machine = sm;
        }
    }
}
