package com.wincom.dcim.agentd.internal.mocks;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class WriteBytesHandlerImpl implements Handler {

    Logger log = LoggerFactory.getLogger(this.getClass());
    private final HandlerContext outbound;

    WriteBytesHandlerImpl(HandlerContext outbound) {
        this.outbound = outbound;
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        outbound.send(m);
    }

}
