package com.wincom.dcim.agentd.messages;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.ChannelInboundHandler;
import io.netty.channel.Channel;

/**
 *
 * @author master
 */
public final class Connected extends ChannelInbound {

    private final Channel channel;

    public Connected(HandlerContext ctx, Channel c) {
        super(ctx);
        this.channel = c;
    }

    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public String toString() {
        return String.format("%s %s", getClass().getSimpleName(), getChannel());
    }

    @Override
    public void applyChannelInbound(HandlerContext ctx, ChannelInboundHandler handler) {
        handler.handleConnected(ctx, this);
    }
}
