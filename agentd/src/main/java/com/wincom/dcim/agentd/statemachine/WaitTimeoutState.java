package com.wincom.dcim.agentd.statemachine;

import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.MillsecFromNowTimeout;
import com.wincom.dcim.agentd.primitives.SetMillsecFromNowTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class WaitTimeoutState extends State.Adapter {

    Logger log = LoggerFactory.getLogger(this.getClass());

    long millsec;
    
    public WaitTimeoutState(long millsec) {
        this.millsec = millsec;
    }
    
    @Override
    public State enter(HandlerContext ctx) {
        ctx.send(new SetMillsecFromNowTimer(millsec));
        ctx.set("timeout.millsec", millsec);
        return this;
    }

    @Override
    public State on(HandlerContext ctx, Message m) {
        log.info(m.toString());
        if (m instanceof MillsecFromNowTimeout) {
            ctx.onSendComplete(m);
            return success();
        } else {
            log.warn("unknown message: " + m);
            return this;
        }
    }

    @Override
    public String toString() {
        return "WaitTimeoutState@" + this.hashCode();
    }
}
