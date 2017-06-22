package com.wincom.dcim.agentd.messages;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.TimerInboundHandler;

/**
 *
 * @author master
 */
public final class PeriodicTimeout extends Timeout {

    public PeriodicTimeout(HandlerContext sender) {
        super(sender);
    }

    @Override
    public void applyTimout(HandlerContext ctx, TimerInboundHandler handler) {
        handler.handlePeriodicTimeout(ctx, this);
    }
}
