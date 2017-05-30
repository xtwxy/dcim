package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public final class ChannelInactive  extends ChannelInbound {

    public ChannelInactive(HandlerContext c) {
        super(c);
    }

    @Override
    public void applyChannelInbound(HandlerContext ctx, ChannelInboundHandler handler) {
        handler.handleChannelInactive(ctx, this);
    }

    @Override
    public String toString() {
        return String.format("%s %s", getClass().getSimpleName(), getContext());
    }
}
