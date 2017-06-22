package com.wincom.dcim.agentd.messages;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.TimerInboundHandler;

/**
 *
 * @author master
 */
public final class DeadlineTimeout extends Timeout {

    public DeadlineTimeout(HandlerContext sender) {
        super(sender);
    }

    @Override
    public void applyTimout(HandlerContext ctx, TimerInboundHandler handler) {
        handler.handleDeadlineTimeout(ctx, this);
    }
}
