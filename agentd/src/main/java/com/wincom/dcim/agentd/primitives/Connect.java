package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.ChannelOutboundHandler;

/**
 *
 * @author master
 */
public final class Connect extends Message.Adapter {

    private final String host;
    private final int port;

    public Connect(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        if (handler instanceof ChannelOutboundHandler) {
            ((ChannelOutboundHandler) handler).handleConnect(ctx, this);
        } else {
            handler.handle(ctx, this);
        }
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
