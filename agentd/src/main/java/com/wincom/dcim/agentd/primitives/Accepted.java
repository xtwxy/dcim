package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public final class Accepted extends ChannelInbound {

    public Accepted(HandlerContext c) {
        super(c);
    }

    @Override
    public void applyChannelInbound(HandlerContext ctx, ChannelInboundHandler handler) {
        handler.handleAccepted(ctx, this);
    }
}
