package com.wincom.dcim.agentd;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.SetDeadlineTimer;
import com.wincom.dcim.agentd.primitives.SetMillsecFromNowTimer;
import com.wincom.dcim.agentd.primitives.SetPeriodicTimer;

/**
 *
 * @author master
 */
public interface TimerOutboundHandler extends Handler {
    public void handleSetDeadlineTimer(HandlerContext ctx, SetDeadlineTimer m);
    public void handleSetMillsecFromNowTimer(HandlerContext ctx, SetMillsecFromNowTimer m);
    public void handleSetPeriodicTimer(HandlerContext ctx, SetPeriodicTimer m);
}
