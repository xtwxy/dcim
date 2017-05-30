package com.wincom.protocol.modbus;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;

/**
 *
 * @author master
 */
public interface ModbusPayloadOutboundHandler extends Handler {
    public void handleReadMultipleHoldingRegistersRequest(HandlerContext ctx, ReadMultipleHoldingRegistersRequest m);
    public void handleWriteMultipleHoldingRegistersRequest(HandlerContext ctx, WriteMultipleHoldingRegistersRequest m);
    public void handleWriteSingleHoldingRegisterRequest(HandlerContext ctx, WriteSingleHoldingRegisterRequest m);
}
