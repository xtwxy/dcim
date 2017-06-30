package com.wincom.dcim.integration.internal;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.Message;

/**
 *
 * @author master
 */
public class TestHandlerContextImpl extends HandlerContext.Adapter {

    TestHandlerContextImpl() {
        inboundHandler = new TestChannelInboundHandlerImpl();
        outboundHandler = new TestChannelOutboundHandlerImpl();
    }
    
    @Override
    public void fire(Message m) {
        m.apply(this, inboundHandler);
    }

}
