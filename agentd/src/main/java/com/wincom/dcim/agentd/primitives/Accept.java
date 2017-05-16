package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class Accept extends Message.Adapter {
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
    public boolean isOob() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("Accept on %s:%d", host, port);
    }
}
