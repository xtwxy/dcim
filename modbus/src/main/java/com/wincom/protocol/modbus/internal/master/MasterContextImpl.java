package com.wincom.protocol.modbus.internal.master;

import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public class MasterContextImpl extends HandlerContext.Adapter {

    public MasterContextImpl(byte slaveAddress) {
        inboundHandler = new MasterPayloadInboundHandlerImpl();
        outboundHandler = new MasterPayloadOutboundHandlerImpl(slaveAddress);
    }
}
