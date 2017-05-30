package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public final class Accept extends Message.Adapter {

    private final String host;
    private final int port;

    public Accept(String host, int port) {
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
    public void apply(HandlerContext ctx, Handler handler) {
        if (handler instanceof ChannelOutboundHandler) {
            ((ChannelOutboundHandler) handler).handleAccept(ctx, this);
        } else {
            handler.handle(ctx, this);
        }
    }

    @Override
    public String toString() {
        return String.format("%s on %s:%d", getClass().getSimpleName(), host, port);
    }
}
