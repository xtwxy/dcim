package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.ChannelInboundHandler;

/**
 *
 * @author master
 */
public final class ChannelActive extends ChannelInbound {

    public ChannelActive(HandlerContext c) {
        super(c);
    }

    @Override
    public void applyChannelInbound(HandlerContext ctx, ChannelInboundHandler handler) {
        handler.handleChannelActive(ctx, this);
    }

    @Override
    public String toString() {
        return String.format("%s %s", getClass().getSimpleName(), getSender());
    }
}
