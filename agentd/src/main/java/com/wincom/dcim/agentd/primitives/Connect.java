package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.ChannelOutboundHandler;

/**
 *
 * @author master
 */
public final class Connect extends ChannelOutbound {

    private final String host;
    private final int port;

    public Connect(HandlerContext c, String host, int port) {
        super(c);
        this.host = host;
        this.port = port;
    }

    @Override
    public void applyChannelOutbound(HandlerContext ctx, ChannelOutboundHandler handler) {
        handler.handleConnect(ctx, this);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return String.format("%s to %s:%d", getClass().getSimpleName(), host, port);
    }
}
