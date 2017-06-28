package com.wincom.dcim.agentd.messages;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.ChannelInboundHandler;

/**
 *
 * @author master
 */
public class SystemError extends ChannelInbound {

    private final Throwable cause;

    public SystemError(HandlerContext c, Throwable cause) {
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
        handler.handleSystemError(ctx, this);
    }
}
