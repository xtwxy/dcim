package com.wincom.dcim.agentd.internal;

import com.google.common.annotations.VisibleForTesting;
import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.CloseConnection;
import com.wincom.dcim.agentd.primitives.ExecuteRunnable;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.Message;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import java.util.HashMap;
import java.util.Map;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.statemachine.StateMachine;

/**
 *
 * @author master
 */
public final class HandlerContextImpl extends HandlerContext.Adapter {

    private Channel channel;
    private final EventLoopGroup eventLoopGroup;
    private final Map<Class, Handler> handlers;

    /**
     * This Constructor is *ONLY* called by internal implementations, 
     * call <code>AgentdService#createHandlerContext()</code> instead.
     * 
     * @param machine
     * @param channel
     * @param eventLoopGroup 
     */
    @VisibleForTesting
    HandlerContextImpl(StateMachine machine,
            Channel channel,
            EventLoopGroup eventLoopGroup
    ) {
        super(machine);
        this.eventLoopGroup = eventLoopGroup;
        this.handlers = new HashMap<>();
        setChannel(channel);
    }
    /**
     * This Constructor is *ONLY* called by internal implementations, 
     * call <code>AgentdService#createHandlerContext()</code> instead.
     * 
     * @param eventLoopGroup 
     */
    @VisibleForTesting
    HandlerContextImpl(
            EventLoopGroup eventLoopGroup
    ) {
        this(new StateMachine(), null, eventLoopGroup);
    }

    @Override
    public void send(Message m) {
        m.apply(this, this.handlers.get(m.getClass()));
    }

    @Override
    public void fire(Message m) {
        getStateMachine().on(this, m);
    }

    private void initHandlers() {
        handlers.put(BytesReceived.class, new SendBytesHandler(channel, eventLoopGroup));
        handlers.put(CloseConnection.class, new CloseConnectionHandler(channel, eventLoopGroup));
        handlers.put(ExecuteRunnable.class, new ExecuteRunnableHandler(channel, eventLoopGroup));
    }

    public final void setChannel(Channel channel) {
        this.channel = channel;
        initHandlers();
    }
}
