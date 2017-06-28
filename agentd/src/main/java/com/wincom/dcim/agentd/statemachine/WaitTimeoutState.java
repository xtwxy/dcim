package com.wincom.dcim.agentd.statemachine;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.messages.MillsecFromNowTimeout;
import com.wincom.dcim.agentd.messages.SetMillsecFromNowTimer;

/**
 *
 * @author master
 */
public class WaitTimeoutState extends State.Adapter {

    private final long millsec;
    
    public WaitTimeoutState(long millsec) {
        this.millsec = millsec;
    }
    
    @Override
    public State enter(HandlerContext ctx) {
        ctx.send(new SetMillsecFromNowTimer(ctx, millsec));
        return this;
    }

    @Override
    public State on(HandlerContext ctx, Message m) {
        log.info(m.toString());
        if (m instanceof MillsecFromNowTimeout) {
            return success();
        } else {
            log.warn(String.format("Unknown state: (%s, %s, %s)", this, ctx, m));
            return this;
        }
    }

    @Override
    public String toString() {
        return "WaitTimeoutState@" + this.hashCode();
    }
}
