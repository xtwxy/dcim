package com.wincom.dcim.agentd.primitives;

import io.netty.channel.Channel;

/**
 *
 * @author master
 */
public class Connected implements Message {
    private final Channel channel;
    public Connected(Channel c) {
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
        return String.format("Connected %s", getChannel());
    }
}
