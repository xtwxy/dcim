package com.wincom.protocol.modbus;

import com.wincom.dcim.agentd.ChannelOutboundHandler;
import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public interface ModbusPayloadOutboundHandler extends ChannelOutboundHandler {
    void handleReadMultipleHoldingRegistersRequest(HandlerContext ctx, ReadMultipleHoldingRegistersRequest m);
    void handleWriteMultipleHoldingRegistersRequest(HandlerContext ctx, WriteMultipleHoldingRegistersRequest m);
    void handleWriteSingleHoldingRegisterRequest(HandlerContext ctx, WriteSingleHoldingRegisterRequest m);
}
