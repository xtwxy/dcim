package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.Unknown;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class DefaultHandlerImpl implements Handler {

    Logger log = LoggerFactory.getLogger(this.getClass());

    DefaultHandlerImpl(HandlerContext outbound) {
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        log.info(String.format("handle(%s, %s)", ctx, m));
        ctx.onRequestCompleted(new Unknown(m));
    }
}
