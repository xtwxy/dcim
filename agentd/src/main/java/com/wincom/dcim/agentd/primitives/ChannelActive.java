package com.wincom.dcim.agentd.primitives;

import io.netty.channel.Channel;

/**
 *
 * @author master
 */
public class ChannelActive implements Message {

    private final Channel channel;

    public ChannelActive(Channel c) {
        this.channel = c;
    }

    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        handler.handle(ctx, this);
    }

    @Override
    public String toString() {
        return String.format("ChannelActive %s", getChannel());
    }
}
