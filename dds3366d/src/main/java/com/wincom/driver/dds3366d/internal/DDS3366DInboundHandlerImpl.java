package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;

/**
 *
 * @author master
 */
public class DDS3366DInboundHandlerImpl extends ChannelInboundHandler.Adapter {

    @Override
    public void handlePayloadReceived(HandlerContext ctx, Message m) {
        ctx.fireInboundHandlerContexts(m);
        ctx.onRequestCompleted(m);
    }
}
