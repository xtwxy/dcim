package com.wincom.dcim.agentd;

import com.wincom.dcim.agentd.messages.DeadlineTimeout;
import com.wincom.dcim.agentd.messages.Handler;
import com.wincom.dcim.agentd.messages.MillsecFromNowTimeout;
import com.wincom.dcim.agentd.messages.PeriodicTimeout;

/**
 *
 * @author master
 */
public interface TimerInboundHandler extends Handler {
    public void handleDeadlineTimeout(HandlerContext ctx, DeadlineTimeout m);
    public void handleMillsecFromNowTimeout(HandlerContext ctx, MillsecFromNowTimeout m);
    public void handlePeriodicTimeout(HandlerContext ctx, PeriodicTimeout m);
}
