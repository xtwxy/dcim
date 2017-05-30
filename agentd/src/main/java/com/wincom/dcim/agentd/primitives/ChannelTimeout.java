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
    public void applyChannelInbound(HandlerContext ctx, ChannelInboundHandler handler) {
        handler.handleChannelTimeout(ctx, this);
    }
}
