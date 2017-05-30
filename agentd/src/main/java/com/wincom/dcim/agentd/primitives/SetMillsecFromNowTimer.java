package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class SetMillsecFromNowTimer extends Message.Adapter {

    private final long millsec;

    public SetMillsecFromNowTimer(long millsec) {
        this.millsec = millsec;
    }

    public long getMillsec() {
        return millsec;
    }

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        if (handler instanceof TimerHandler) {
            ((TimerHandler) handler).handleSetMillsecFromNowTimer(ctx, this);
        } else {
            handler.handle(ctx, this);
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s millsec from now.", getClass().getSimpleName(), millsec);
    }
}
