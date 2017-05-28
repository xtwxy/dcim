package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class Connect extends Message.Adapter {

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
    public boolean isOob() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("Connect to %s:%d", host, port);
    }
}
