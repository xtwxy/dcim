package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.ChannelInboundHandler;

/**
 *
 * @author master
 */
public final class Failed extends ChannelInbound {

    private Throwable cause;

    public Failed(HandlerContext c, Throwable cause) {
        super(c);
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }

    @Override
    public String toString() {
        return String.format("%s %s", getClass().getSimpleName(), cause);
    }

    @Override
    public void applyChannelInbound(HandlerContext ctx, ChannelInboundHandler handler) {
        handler.handleFailed(ctx, this);
    }
}
