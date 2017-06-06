package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.ChannelOutboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;

/**
 *
 * @author master
 */
public class DDS3366DOutboundHandlerImpl extends ChannelOutboundHandler.Adapter {

    @Override
    public void handleSendPayload(HandlerContext ctx, Message m) {
        // 1.find request.
        // 2.execute request
        // 2.1.check availability/dependency?
        // 2.2.send
        // 2.3.recv
        // 3.reply
    }
}
