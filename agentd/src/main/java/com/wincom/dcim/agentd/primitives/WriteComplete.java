package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public final class WriteComplete extends ChannelInbound {

    public WriteComplete(HandlerContext c) {
        super(c);
    }

    @Override
    public void applyChannelInbound(HandlerContext ctx, ChannelInboundHandler handler) {
        handler.handlePayloadSent(ctx, this);
    }
}
