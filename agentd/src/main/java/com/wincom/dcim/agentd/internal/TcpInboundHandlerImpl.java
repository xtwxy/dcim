package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.NetworkService;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wincom.dcim.agentd.primitives.ChannelInboundHandler;

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
    public String toString() {
        return "TcpInboundHandlerImpl@" + this.hashCode();
    }
}
