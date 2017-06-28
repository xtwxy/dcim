package com.wincom.dcim.agentd.messages;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.ChannelInboundHandler;

/**
 *
 * @author master
 */
public final class ApplicationFailure extends ChannelInbound {

    private final Throwable cause;

    public ApplicationFailure(HandlerContext c, Throwable cause) {
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
        handler.handleApplicationFailure(ctx, this);
    }
}
