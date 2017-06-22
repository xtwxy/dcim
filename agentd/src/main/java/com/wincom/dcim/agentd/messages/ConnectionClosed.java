package com.wincom.dcim.agentd.messages;

import com.wincom.dcim.agentd.HandlerContext;
import io.netty.channel.Channel;

/**
 *
 * @author master
 */
public final class ConnectionClosed extends Message.Adapter {

    private final Channel channel;

    public ConnectionClosed(HandlerContext sender, Channel c) {
        super(sender);
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
