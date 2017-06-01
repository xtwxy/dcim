package com.wincom.protocol.modbus.internal.mocks;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class DefaultHandlerImpl implements Handler {

    Logger log = LoggerFactory.getLogger(this.getClass());
    private final HandlerContext outbound;

    DefaultHandlerImpl(HandlerContext outbound) {
        this.outbound = outbound;
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        log.info(String.format("handle(%s, %s)", ctx, m));
    }
}
