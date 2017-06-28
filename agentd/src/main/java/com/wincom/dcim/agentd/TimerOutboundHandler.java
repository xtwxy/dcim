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
    void handleSetDeadlineTimer(HandlerContext ctx, SetDeadlineTimer m);
    void handleSetMillsecFromNowTimer(HandlerContext ctx, SetMillsecFromNowTimer m);
    void handleSetPeriodicTimer(HandlerContext ctx, SetPeriodicTimer m);
}
