package com.wincom.dcim.agentd.internal.tests;

import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.MillsecFromNowTimeout;
import com.wincom.dcim.agentd.primitives.SetMillsecFromNowTimer;
import com.wincom.dcim.agentd.statemachine.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class WaitTimeoutState extends State.Adapter {

    Logger log = LoggerFactory.getLogger(this.getClass());

    HandlerContext handlerContext;
    long millsec;
    
    public WaitTimeoutState(HandlerContext handlerContext, long millsec) {
        this.handlerContext = handlerContext;
        this.millsec = millsec;
    }
    
    @Override
    public State enter() {
        this.handlerContext.send(new SetMillsecFromNowTimer(millsec));
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
