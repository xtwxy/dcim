package com.wincom.protocol.modbus.internal.master;

import java.util.Map;

import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public class MasterDecodeContextImpl extends HandlerContext.Adapter {

    MasterDecodeContextImpl(Map<Byte, MasterContextImpl> inboundContexts) {
        inboundHandler = new MasterDecodeInboundHandlerImpl(inboundContexts);
        outboundHandler = new MasterDecodeOutboundHandlerImpl();
    }

    @Override
    public boolean isInprogress() {
        return !state().stopped();
    }
}
