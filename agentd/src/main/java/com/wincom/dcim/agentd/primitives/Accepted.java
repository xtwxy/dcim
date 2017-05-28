package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class Accepted extends ChannelInbound {

    public Accepted(HandlerContext c) {
        super(c);
    }

    @Override
    public boolean isOob() {
        return true;
    }

    @Override
    public void applyChannelInbound(HandlerContext ctx, ChannelInboundHandler handler) {
        handler.handleAccepted(ctx, this);
    }

    @Override
    public String toString() {
        return String.format("Accepted %s", getContext());
    }
}
