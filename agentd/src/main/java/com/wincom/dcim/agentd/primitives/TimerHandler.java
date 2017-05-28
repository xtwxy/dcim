package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public interface TimerHandler extends Handler {
    public void handleSetDeadlineTimer(HandlerContext ctx, SetDeadlineTimer m);
    public void handleSetMillsecFromNowTimer(HandlerContext ctx, SetMillsecFromNowTimer m);
    public void handleSetPeriodicTimer(HandlerContext ctx, SetPeriodicTimer m);
}
