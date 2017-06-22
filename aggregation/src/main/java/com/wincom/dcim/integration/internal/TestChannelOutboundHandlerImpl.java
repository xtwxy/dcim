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
        log.info(String.format("handleSendPayload(%s, %s)", ctx, m));
        outboundContext.send(m);
    }

}
