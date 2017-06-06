package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public class DDS3366DHandlerContextImpl extends HandlerContext.Adapter {

    public DDS3366DHandlerContextImpl() {
        inboundHandler = new PayloadInboundHandlerImpl();
        outboundHandler = new PayloadOutboundHandlerImpl();
    }
}
