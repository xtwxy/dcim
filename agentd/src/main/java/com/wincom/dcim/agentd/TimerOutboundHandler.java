package com.wincom.dcim.agentd;

import com.wincom.dcim.agentd.messages.Handler;
import com.wincom.dcim.agentd.messages.SetDeadlineTimer;
import com.wincom.dcim.agentd.messages.SetMillsecFromNowTimer;
import com.wincom.dcim.agentd.messages.SetPeriodicTimer;

/**
 *
 * @author master
 */
public interface TimerOutboundHandler extends Handler {
    public void handleSetDeadlineTimer(HandlerContext ctx, SetDeadlineTimer m);
    public void handleSetMillsecFromNowTimer(HandlerContext ctx, SetMillsecFromNowTimer m);
    public void handleSetPeriodicTimer(HandlerContext ctx, SetPeriodicTimer m);
}
