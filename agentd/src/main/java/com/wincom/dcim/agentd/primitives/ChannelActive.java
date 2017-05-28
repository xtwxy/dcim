package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class ChannelActive extends ChannelInbound {

    public ChannelActive(HandlerContext c) {
        super(c);
    }

    @Override
    public boolean isOob() {
        return true;
    }

    @Override
    public void applyChannelInbound(HandlerContext ctx, ChannelInboundHandler handler) {
        handler.handleChannelActive(ctx, this);
    }

    @Override
    public String toString() {
        return String.format("ChannelActive %s", getContext());
    }
}
