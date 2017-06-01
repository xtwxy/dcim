package com.wincom.dcim.agentd;

import com.wincom.dcim.agentd.primitives.DeadlineTimeout;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.MillsecFromNowTimeout;
import com.wincom.dcim.agentd.primitives.PeriodicTimeout;

/**
 *
 * @author master
 */
public interface TimerInboundHandler extends Handler {
    public void handleDeadlineTimeout(HandlerContext ctx, DeadlineTimeout m);
    public void handleMillsecFromNowTimeout(HandlerContext ctx, MillsecFromNowTimeout m);
    public void handlePeriodicTimeout(HandlerContext ctx, PeriodicTimeout m);
}
