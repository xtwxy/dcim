package com.wincom.dcim.agentd.primitives;

import io.netty.channel.Channel;

/**
 *
 * @author master
 */
public final class ConnectionClosed extends Message.Adapter {

    private final Channel channel;

    public ConnectionClosed(Channel c) {
        this.channel = c;
    }

    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public String toString() {
        return String.format("ConnectionClosed %s", getChannel());
    }
}
