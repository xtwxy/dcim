package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.primitives.Accepted;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wincom.dcim.agentd.primitives.ChannelInboundHandler;
import com.wincom.dcim.agentd.primitives.Connected;
import com.wincom.dcim.agentd.primitives.HandlerContext;

/**
 *
 * @author master
 */
public final class TcpInboundHandlerImpl extends ChannelInboundHandler.Adapter {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private Channel channel;
    private final NetworkService service;

    TcpInboundHandlerImpl(NetworkService service) {
        this.service = service;
    }

    public final void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void handleAccepted(HandlerContext ctx, Accepted m) {
        log.info(String.format("handleAccepted(%s, %s)", ctx, m));
    }

    @Override
    public void handleConnected(HandlerContext ctx, Connected m) {
    }

    @Override
    public String toString() {
        return "TcpInboundHandlerImpl@" + this.hashCode();
    }
}
