package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class ChannelInactive  extends ChannelInbound {

    public ChannelInactive(HandlerContext c) {
        super(c);
    }

    @Override
    public boolean isOob() {
        return true;
    }

    @Override
    public void applyChannelInbound(HandlerContext ctx, ChannelInboundHandler handler) {
        handler.handleChannelInactive(ctx, this);
    }

    @Override
    public String toString() {
        return String.format("ChannelInactive %s", getContext());
    }
}
