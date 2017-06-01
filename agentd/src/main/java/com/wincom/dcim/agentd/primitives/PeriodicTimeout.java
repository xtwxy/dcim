package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.TimerInboundHandler;

/**
 *
 * @author master
 */
public final class PeriodicTimeout extends Timeout {

    @Override
    public void applyTimout(HandlerContext ctx, TimerInboundHandler handler) {
        handler.handlePeriodicTimeout(ctx, this);
    }
}
