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
        this.outboundHandler = new TcpOutboundHandlerImpl(service);
        this.inboundHandler = new TcpInboundHandlerImpl();
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
        ((TcpOutboundHandlerImpl) outboundHandler).setChannel(channel);
    }

    public Channel getChannel() {
        return channel;
    }
}
