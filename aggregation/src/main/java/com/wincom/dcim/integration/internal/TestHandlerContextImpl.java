package com.wincom.dcim.integration.internal;

import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public class TestHandlerContextImpl extends HandlerContext.Adapter {

    TestHandlerContextImpl() {
        inboundHandler = new TestChannelInboundHandlerImpl();
        outboundHandler = new TestChannelOutboundHandlerImpl();
    }
}
