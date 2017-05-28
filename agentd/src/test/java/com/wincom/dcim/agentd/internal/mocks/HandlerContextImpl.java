package com.wincom.dcim.agentd.internal.mocks;

import com.wincom.dcim.agentd.primitives.HandlerContext;

/**
 *
 * @author master
 */
public class HandlerContextImpl extends HandlerContext.Adapter {
    public HandlerContextImpl() {
        setOutboundHandler(new OutboundHandlerImpl());
    }
}
