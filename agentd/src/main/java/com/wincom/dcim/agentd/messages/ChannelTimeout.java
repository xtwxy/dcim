package com.wincom.dcim.agentd.messages;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.ChannelInboundHandler;

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
