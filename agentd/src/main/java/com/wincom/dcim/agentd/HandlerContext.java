package com.wincom.dcim.agentd;

import com.wincom.dcim.agentd.messages.ChannelActive;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author master
 */
public interface HandlerContext {

    /**
     * Send message via the underlying service.
     *
     * @param m
     */
    void send(Message m);

    /**
     * Called by the sender when a message is processed, the result maybe
     * success or not.
     */
    void onRequestCompleted();

    /**
     * Fire message from the underlying service to the state machine.
     *
     * @param m
     */
    void fire(Message m);

    /**
     * Fire message to upper layer inbound handler contexts.
     *
     * @param m
     */
    void fireInboundHandlerContexts(Message m);

    /**
     * Set context variables.
     *
     * @param key
     * @param value
     */
    void set(Object key, Object value);

    Object get(Object key);

    /**
     * Get context variables.
     *
     * @param key
     * @return
     */
    Object get(Object key, Object defaultValue);

    /**
     * Get context variable if exists, and set default value and return the
     * default set if not.
     *
     * @param key
     * @param defaultValue
     * @return
     */
    Object getOrSetIfNotExist(Object key, Object defaultValue);

    /**
     * Remove context variables.
     *
     * @param key
     * @return
     */
    Object remove(Object key);

    /**
     * Test if channel is ready to send & receive.
     *
     * @return
     */
    boolean isActive();

    void setActive(boolean b);

    void addInboundContext(HandlerContext ctx);

    void removeInboundContext(HandlerContext ctx);

    ChannelInboundHandler getInboundHandler();

    ChannelOutboundHandler getOutboundHandler();

    void onClosed(Message m);

    void release();

    void dispose();

    void addDisposeHandler(DisposeHandler h);

    void state(State sm);

    State state();

    interface DisposeHandler {
        void onDispose(HandlerContext ctx);
    }

    class Adapter implements HandlerContext {

        protected final Logger log = LoggerFactory.getLogger(this.getClass());

        protected volatile State state;
        private final Map<Object, Object> variables;
        protected final ConcurrentLinkedQueue<Message> queue;
        protected volatile Message current;
        private volatile boolean active;
        protected ChannelOutboundHandler outboundHandler;
        protected ChannelInboundHandler inboundHandler;
        protected final Set<HandlerContext> inboundHandlers;
        protected final Set<DisposeHandler> disposeHandlers;

        public Adapter() {
            this.state = new StateMachine();
            this.variables = new HashMap<>();
            this.queue = new ConcurrentLinkedQueue<>();
            this.inboundHandlers = new LinkedHashSet<>();
            this.disposeHandlers = new LinkedHashSet<>();
            this.current = null;
            this.active = false;
        }

        @Override
        public void addInboundContext(HandlerContext ctx) {
            if (ctx != null) {
                synchronized (inboundHandlers) {
                    inboundHandlers.add(ctx);
                }
            }
            if (isActive()) {
                Message m = new ChannelActive(this);
                ctx.fire(m);
            }
        }

        @Override
        public void removeInboundContext(HandlerContext ctx) {
            synchronized (inboundHandlers) {
                inboundHandlers.remove(ctx);
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
        public void send(Message m) {
            log.info("send(): message = {},  current = {}, isActive() = {}, queue = {}", m, current, isActive(), queue);
            synchronized (queue) {
                queue.add(m);
            }
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
            synchronized (inboundHandlers) {
                for (HandlerContext ctx : inboundHandlers) {
                    ctx.fire(m);
                }
            }
        }

        @Override
        public void onClosed(Message m) {
            setActive(false);
            onRequestCompleted();
        }

        @Override
        public void onRequestCompleted() {
            log.info("onRequestCompleted(): current = {}, isActive() = {}, queue = {}", current, isActive(), queue);
            if (isInprogress()) {
                current = null;
            } else {
                // not possible! should be discarded!
            }
            sendNext();
        }

        protected void sendNext() {
            //synchronized (queue)
            log.info("sendNext(): current = {}, isActive() = {}, queue = {}", current, isActive(), queue);
            synchronized (queue) {
                if (!queue.isEmpty()) {
                    current = queue.poll();
                }
            }
            if(current != null) {
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
        public synchronized boolean isActive() {
            return active;
        }

        @Override
        public synchronized void setActive(boolean b) {
            this.active = b;
            if (!isInprogress()) {
                sendNext();
            }
        }

        @Override
        public void state(State sm) {
            state = sm;
        }

        @Override
        public State state() {
            return this.state;
        }

        @Override
        public void release() {
        }

        @Override
        public void dispose() {
            for (DisposeHandler h : disposeHandlers) {
                h.onDispose(this);
            }
        }

        @Override
        public void addDisposeHandler(DisposeHandler h) {
            disposeHandlers.add(h);
        }
    }
}
