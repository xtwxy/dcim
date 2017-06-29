package com.wincom.dcim.integration.internal;

import com.wincom.dcim.agentd.ChannelOutboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.Message;

/**
 *
 * @author master
 */
public class TestChannelOutboundHandlerImpl extends ChannelOutboundHandler.Adapter {

    @Override
    public void handleSendPayload(HandlerContext ctx, Message m) {
        if(outboundContext == null) {
            outboundContext.send(m);
        } else {
        }
    }

}
