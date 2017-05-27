package com.wincom.protocol.modbus.internal.mocks;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;

/**
 *
 * @author master
 */
public class InboundHandlerImpl implements Handler {

    private final HandlerContext inboundContext;

    public InboundHandlerImpl(HandlerContext inboundContext) {
        this.inboundContext = inboundContext;
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        inboundContext.fire(m);
    }

}
