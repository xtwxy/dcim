package com.wincom.protocol.modbus;

import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public interface ModbusPayloadInboundHandler extends ChannelInboundHandler {
    public void handleReadMultipleHoldingRegistersResponse(HandlerContext ctx, ReadMultipleHoldingRegistersResponse m);
    public void handleWriteMultipleHoldingRegistersResponse(HandlerContext ctx, WriteMultipleHoldingRegistersResponse m);
    public void handleWriteSingleHoldingRegisterResponse(HandlerContext ctx, WriteSingleHoldingRegisterResponse m);
}
