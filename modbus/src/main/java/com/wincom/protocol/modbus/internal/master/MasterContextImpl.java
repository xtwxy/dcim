package com.wincom.protocol.modbus.internal.master;

import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public class MasterContextImpl extends HandlerContext.Adapter {

    private final byte slaveAddress;

    public MasterContextImpl(byte slaveAddress) {
        this.slaveAddress = slaveAddress;
        inboundHandler = new MasterPayloadInboundHandlerImpl();
        outboundHandler = new MasterPayloadOutboundHandlerImpl(slaveAddress);
    }
}
