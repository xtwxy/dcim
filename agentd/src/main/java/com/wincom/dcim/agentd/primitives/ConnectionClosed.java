package com.wincom.dcim.agentd.primitives;

import io.netty.channel.Channel;

/**
 *
 * @author master
 */
public class ConnectionClosed implements Message {
    private final Channel channel;
    public ConnectionClosed(Channel c) {
        this.channel = c;
    }
    
    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        handler.handle(ctx, this);
    }
}
