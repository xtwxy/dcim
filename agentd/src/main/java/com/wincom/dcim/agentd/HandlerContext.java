package com.wincom.dcim.agentd;

import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.Message;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
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
     * Fire message to upper layer inbound handler contexts.
     * 
     * @param m 
     */
    public void fireInboundHandlerContexts(Message m);
    
    /**
     * Set context variables.
     *
     * @param key
     * @param value
     */
    public void set(Object key, Object value);

    public Object get(Object key);

    /**
     * Get context variables.
     *
     * @param key
     * @return
     */
    public Object get(Object key, Object defaultValue);

    /**
     * Get context variable if exists, and set default value and return the
     * default set if not.
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public Object getOrSetIfNotExist(Object key, Object defaultValue);

    /**
     * Remove context variables.
     *
     * @param key
     * @return
     */
    public Object remove(Object key);

    /**
     * Test if channel is ready to send & receive.
     *
     * @return
     */
    public boolean isActive();

    public void setActive(boolean b);

    public void addInboundContext(HandlerContext ctx);

    public ChannelInboundHandler getInboundHandler();
    public ChannelOutboundHandler getOutboundHandler();

    public void onClosed(Message m);

    public static class Adapter implements HandlerContext {

        protected Logger log = LoggerFactory.getLogger(this.getClass());

        private final Map<Object, Object> variables;
        protected final ConcurrentLinkedQueue<Message> queue;
        protected Message current;
        private boolean active;
        protected ChannelOutboundHandler outboundHandler;
        protected ChannelInboundHandler inboundHandler;
        protected Set<HandlerContext> inboundHandlers;

        public Adapter() {
            this.variables = new HashMap<>();
            this.queue = new ConcurrentLinkedQueue<>();
            this.inboundHandlers = new LinkedHashSet<>();
            this.current = null;
            this.active = false;
        }

        @Override
        public synchronized void addInboundContext(HandlerContext ctx) {
            if(ctx != null) {
                inboundHandlers.add(ctx);
            }
            if (isActive()) {
                Message m = new ChannelActive(this);
                ctx.fire(m);
            }
        }

        @Override
        public ChannelOutboundHandler getOutboundHandler() {
            return this.outboundHandler;
        }

        @Override
        public ChannelInboundHandler getInboundHandler() {
            return this.inboundHandler;
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
            m.apply(this, inboundHandler);
        }
        
        @Override
        public void fireInboundHandlerContexts(Message m) {
            for (HandlerContext ctx : inboundHandlers) {
                ctx.fire(m);
            }
        }

        @Override
        public synchronized void onClosed(Message m) {
            setActive(false);
            onRequestCompleted(m);
            /*
             * Do not empty the queue!
             */
 /* while (!queue.isEmpty()) {
             *   Message s = queue.poll();
             * }
             */
        }

        @Override
        public synchronized void onRequestCompleted(Message m) {
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
        public final void set(Object key, Object value) {
            variables.put(key, value);
        }

        @Override
        public final Object get(Object key) {
            return variables.get(key);
        }

        @Override
        public final Object get(Object key, Object defaultValue) {
            return variables.getOrDefault(key, defaultValue);
        }

        @Override
        public final Object getOrSetIfNotExist(Object key, Object defaultValue) {
            Object v = variables.get(key);
            if (v == null) {
                variables.put(key, defaultValue);
                return defaultValue;
            } else {
                return v;
            }
        }

        @Override
        public final Object remove(Object key) {
            return variables.remove(key);
        }

        public boolean isInprogress() {
            return this.current != null;
        }

        @Override
        public boolean isActive() {
            return active;
        }

        @Override
        public synchronized void setActive(boolean b) {
            this.active = b;
            if (!isInprogress()) {
                sendNext();
            }
        }
    }
}
