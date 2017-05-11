package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class Connect implements Message {

    private final String host;
    private final int port;

    public Connect(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        handler.handle(ctx, this);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return String.format("Connect to %s:%d", host, port);
    }
}
