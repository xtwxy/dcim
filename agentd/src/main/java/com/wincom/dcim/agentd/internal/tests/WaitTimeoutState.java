package com.wincom.dcim.agentd.internal.tests;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SetOnetimeTimer;
import com.wincom.dcim.agentd.primitives.Timeout;
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
    int seconds;
    
    public WaitTimeoutState(HandlerContext handlerContext, int seconds) {
        this.handlerContext = handlerContext;
        this.seconds = seconds;
    }
    
    @Override
    public State enter() {
        this.handlerContext.send(new SetOnetimeTimer(60000));
        return this;
    }

    @Override
    public State on(HandlerContext ctx, Message m) {
        log.info(m.toString());
        if (m instanceof Timeout) {
            ctx.onSendComplete(m);
            return success();
        } else {
            log.warn("unknown message: " + m);
            return success();
        }
    }
}
