package com.wincom.protocol.modbus;

import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public interface ModbusPayloadInboundHandler extends ChannelInboundHandler {
    void handleReadMultipleHoldingRegistersResponse(HandlerContext ctx, ReadMultipleHoldingRegistersResponse m);
    void handleWriteMultipleHoldingRegistersResponse(HandlerContext ctx, WriteMultipleHoldingRegistersResponse m);
    void handleWriteSingleHoldingRegisterResponse(HandlerContext ctx, WriteSingleHoldingRegisterResponse m);
}
