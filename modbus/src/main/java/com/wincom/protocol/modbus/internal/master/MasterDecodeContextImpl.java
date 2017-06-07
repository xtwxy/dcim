package com.wincom.protocol.modbus.internal.master;

import com.wincom.dcim.agentd.HandlerContext;
import java.util.Map;

/**
 *
 * @author master
 */
public class MasterDecodeContextImpl extends HandlerContext.Adapter {

    MasterDecodeContextImpl(Map<Byte, MasterContextImpl> inboundContexts) {
        inboundHandler = new MasterDecodeInboundHandlerImpl(inboundContexts);
        outboundHandler = new MasterDecodeOutboundHandlerImpl(inboundContexts);
    }

    @Override
    public boolean isInprogress() {
        return !state().stopped();
    }
}
