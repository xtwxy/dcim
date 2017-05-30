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

    public void setInboundHandler(Handler handler);

    public Handler getInboundHandler();

    public void setOutboundHandler(Handler handler);

    public Handler getOutboundHandler();

    public void fireClosed(Message m);

    public static class Adapter implements HandlerContext {

        protected Logger log = LoggerFactory.getLogger(this.getClass());

        protected StateMachine machine;
        private final Map<Object, Object> variables;
        protected final ConcurrentLinkedQueue<Message> queue;
        protected Message current;
        private boolean active;
        protected Handler outboundHandler;
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
            Handler old = this.inboundHandler;
            this.inboundHandler = handler;
            if (isActive()) {
                if (old != handler) {
                    if (old != null) {
                        Message m = new ChannelInactive(this);
                        m.apply(this, old);
                    }
                }
                Message m = new ChannelActive(this);
                m.apply(this, this.inboundHandler);
            }
        }

        @Override
        public void setOutboundHandler(Handler handler) {
            this.outboundHandler = handler;
        }

        @Override
        public Handler getOutboundHandler() {
            return this.outboundHandler;
        }

        @Override
        public synchronized void send(Message m) {
            queue.add(m);
            if (!isInprogress()) {
                sendNext();
            } else {
                // wait for complete
            }
        }

        @Override
        public void fire(Message m) {
            m.apply(this, getInboundHandler());
            machine.on(this, m);
        }

        @Override
        public void fireClosed(Message m) {
            active = false;
            if (isInprogress()) {
                current = null;
            }

            while (!queue.isEmpty()) {
                Message s = queue.poll();
            }
        }

        @Override
        public synchronized void onRequestCompleted(Message response) {
            if (isInprogress()) {
                current = null;
            } else {
                // not possible! should be discarded!
            }
            sendNext();
        }

        protected void sendNext() {
            //synchronized (queue)
            if (!queue.isEmpty()) {
                current = queue.poll();
                current.apply(this, this.outboundHandler);
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
            if (!isInprogress()) {
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
        public void fire(Message m) {
            log.info(String.format("fire(%s)", m));
        }
    }
}
