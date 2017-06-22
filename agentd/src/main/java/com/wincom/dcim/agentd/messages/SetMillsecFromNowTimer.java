package com.wincom.dcim.agentd.messages;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.TimerOutboundHandler;

/**
 *
 * @author master
 */
public class SetMillsecFromNowTimer extends Message.Adapter {

    private final long millsec;

    public SetMillsecFromNowTimer(HandlerContext sender, long millsec) {
        super(sender);
        this.millsec = millsec;
    }

    public long getMillsec() {
        return millsec;
    }

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        if (handler instanceof TimerOutboundHandler) {
            ((TimerOutboundHandler) handler).handleSetMillsecFromNowTimer(ctx, this);
        } else {
            handler.handle(ctx, this);
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s millsec from now.", getClass().getSimpleName(), millsec);
    }
}
