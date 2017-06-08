package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.ChannelInboundHandler;

/**
 *
 * @author master
 */
public final class Accepted extends ChannelInbound {
    private final HandlerContext accepted;
    public Accepted(HandlerContext sender, HandlerContext accepted) {
        super(sender);
        this.accepted = accepted;
    }

    @Override
    public void applyChannelInbound(HandlerContext ctx, ChannelInboundHandler handler) {
        handler.handleAccepted(ctx, this);
    }

    public HandlerContext getAccepted() {
        return accepted;
    }
}
