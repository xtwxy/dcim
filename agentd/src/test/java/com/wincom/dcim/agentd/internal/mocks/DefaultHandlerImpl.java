package com.wincom.dcim.agentd.internal.mocks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.Handler;
import com.wincom.dcim.agentd.messages.Message;

/**
 *
 * @author master
 */
public class DefaultHandlerImpl implements Handler {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(HandlerContext ctx, Message m) {
        log.info(String.format("handle(%s, %s)", ctx, m));
    }
}
