package com.wincom.protocol.modbus.internal.master;

import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;

/**
 *
 * @author master
 */
public class MasterPayloadInboundHandlerImpl
        extends ChannelInboundHandler.Adapter {

    @Override
    public void handlePayloadReceived(HandlerContext ctx, Message m) {
        ctx.fireInboundHandlerContexts(m);
        ctx.onRequestCompleted(m);
    }

    @Override
    public void handlePayloadSent(HandlerContext ctx, Message m) {
    }
}
