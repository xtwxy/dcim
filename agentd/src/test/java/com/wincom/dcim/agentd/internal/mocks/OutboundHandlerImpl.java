package com.wincom.dcim.agentd.internal.mocks;

import com.wincom.dcim.agentd.ChannelOutboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.Message;

/**
 *
 * @author master
 */
public class OutboundHandlerImpl extends ChannelOutboundHandler.Adapter {

    private HandlerContext outbound;

    public void setOutboundContext(HandlerContext delegate) {
        this.outbound = delegate;
    }

    @Override
    public void handleSendPayload(HandlerContext ctx, Message m) {
        outbound.send(m);
    }
}
