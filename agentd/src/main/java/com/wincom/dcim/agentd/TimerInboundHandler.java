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
    void handleDeadlineTimeout(HandlerContext ctx, DeadlineTimeout m);
    void handleMillsecFromNowTimeout(HandlerContext ctx, MillsecFromNowTimeout m);
    void handlePeriodicTimeout(HandlerContext ctx, PeriodicTimeout m);
}
