package com.wincom.dcim.agentd.primitives;

import io.netty.channel.Channel;

/**
 *
 * @author master
 */
public class ChannelActive extends Message.Adapter {

    private final Channel channel;

    public ChannelActive(Channel c) {
        this.channel = c;
    }

    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public boolean isOob() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("ChannelActive %s", getChannel());
    }
}
