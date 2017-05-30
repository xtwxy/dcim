package com.wincom.protocol.modbus;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;

/**
 *
 * @author master
 */
public interface ModbusPayloadInboundHandler extends Handler {
    public void handleReadMultipleHoldingRegistersResponse(HandlerContext ctx, ReadMultipleHoldingRegistersResponse m);
    public void handleWriteMultipleHoldingRegistersResponse(HandlerContext ctx, WriteMultipleHoldingRegistersResponse m);
    public void handleWriteSingleHoldingRegisterResponse(HandlerContext ctx, WriteSingleHoldingRegisterResponse m);
}
