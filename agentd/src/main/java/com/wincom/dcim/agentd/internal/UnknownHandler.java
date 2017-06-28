package com.wincom.dcim.agentd.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.Handler;
import com.wincom.dcim.agentd.messages.Message;

/**
 *
 * @author master
 */
public class UnknownHandler implements Handler {

    Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public void handle(HandlerContext ctx, Message m) {
        log.warn(String.format("handle(%s, %s, %s)", ctx, m, m.getClass().getName()));
        new Exception().printStackTrace();
        ctx.onRequestCompleted();
        ctx.fireInboundHandlerContexts(m);
    }

}
