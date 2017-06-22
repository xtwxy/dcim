package com.wincom.dcim.agentd.messages;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.ChannelOutboundHandler;

/**
 *
 * @author master
 */
public final class Accept extends ChannelOutbound {

    private final String host;
    private final int port;

    public Accept(HandlerContext c, String host, int port) {
        super(c);
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public void applyChannelOutbound(HandlerContext ctx, ChannelOutboundHandler handler) {
        handler.handleAccept(ctx, this);
    }

    @Override
    public String toString() {
        return String.format("%s on %s:%d", getClass().getSimpleName(), host, port);
    }
}
