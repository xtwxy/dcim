package com.wincom.dcim.agentd.internal.mocks;

import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public class HandlerContextImpl extends HandlerContext.Adapter {
    public HandlerContextImpl() {
        inboundHandler = new InboundHandlerImpl();
        outboundHandler = new OutboundHandlerImpl();
    }
}
