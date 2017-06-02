package com.wincom.dcim.agentd.internal;

import com.google.common.annotations.VisibleForTesting;
import com.wincom.dcim.agentd.NetworkService;
import io.netty.channel.Channel;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.statemachine.StateMachine;

/**
 *
 * @author master
 */
public final class StreamHandlerContextImpl
        extends HandlerContext.Adapter {

    private Channel channel;
    private final NetworkService service;

    @VisibleForTesting
    StreamHandlerContextImpl(
            NetworkService service
    ) {
        this(new StateMachine(), service);
    }

    @VisibleForTesting
    StreamHandlerContextImpl(StateMachine sm,
            NetworkService service
    ) {
        this.service = service;
        this.outboundHandler = new TcpOutboundHandlerImpl(service);
        this.inboundHandler = new TcpInboundHandlerImpl(service);
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
        ((TcpOutboundHandlerImpl) outboundHandler).setChannel(channel);
        ((TcpInboundHandlerImpl) inboundHandler).setChannel(channel);
    }

    public Channel getChannel() {
        return channel;
    }
}
