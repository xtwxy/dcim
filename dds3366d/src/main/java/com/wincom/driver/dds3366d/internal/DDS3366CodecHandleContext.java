package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public class DDS3366CodecHandleContext extends HandlerContext.Adapter {

    public DDS3366CodecHandleContext() {
        inboundHandler = new PayloadInboundHandlerImpl();
        outboundHandler = new PayloadOutboundHandlerImpl();
    }
}
