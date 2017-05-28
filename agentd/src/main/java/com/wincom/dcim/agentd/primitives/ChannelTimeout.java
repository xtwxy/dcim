package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class ChannelTimeout extends ChannelInbound {
    public ChannelTimeout(HandlerContext c) {
        super(c);
    }

    @Override
    public boolean isOob() {
        return true;
    }

    @Override
    public void applyChannelInbound(HandlerContext ctx, ChannelInboundHandler handler) {
        handler.handleChannelTimeout(ctx, this);
    }

    @Override
    public String toString() {
        return String.format("ChannelTimeout %s", getContext());
    }
}
