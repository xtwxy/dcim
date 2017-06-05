package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public final class WriteTimeout extends ChannelTimeout {

    public WriteTimeout(HandlerContext c) {
        super(c);
    }

    @Override
    public void applyChannelInbound(HandlerContext ctx, ChannelInboundHandler handler) {
        handler.handleChannelWriteTimeout(ctx, this);
    }
}
