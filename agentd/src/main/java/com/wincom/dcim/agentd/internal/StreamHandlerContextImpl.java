package com.wincom.dcim.agentd.internal;

import com.google.common.annotations.VisibleForTesting;
import com.wincom.dcim.agentd.NetworkService;
import io.netty.channel.Channel;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public final class StreamHandlerContextImpl
        extends HandlerContext.Adapter {

    Logger log = LoggerFactory.getLogger(this.getClass());

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
        super(sm);
        this.service = service;
        this.outboundHandler = new TcpOutboundHandlerImpl(service);
        this.inboundHandler = new TcpInboundHandlerImpl(service);
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
        if(outboundHandler != null) {
            ((TcpOutboundHandlerImpl)outboundHandler).setChannel(channel);
        }
        if(inboundHandler instanceof TcpInboundHandlerImpl) {
            ((TcpInboundHandlerImpl)inboundHandler).setChannel(channel);
        }
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return "StreamHandlerContextImpl@" + this.hashCode();
    }
}
