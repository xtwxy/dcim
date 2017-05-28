package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class WriteComplete extends ChannelInbound {

    public WriteComplete(HandlerContext c) {
        super(c);
    }

    @Override
    public boolean isOob() {
        return true;
    }

    @Override
    public void applyChannelInbound(HandlerContext ctx, ChannelInboundHandler handler) {
        handler.handlePayloadSent(ctx, this);
    }

    @Override
    public String toString() {
        return String.format("WriteComplete %s", getContext());
    }
}
