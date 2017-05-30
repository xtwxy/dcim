package com.wincom.dcim.agentd.primitives;

import io.netty.channel.Channel;

/**
 *
 * @author master
 */
public final class Connected extends Message.Adapter {

    private final Channel channel;

    public Connected(Channel c) {
        this.channel = c;
    }

    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public String toString() {
        return String.format("%s %s", getClass().getSimpleName(), getChannel());
    }
}
