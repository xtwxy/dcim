package com.wincom.protocol.modbus.internal.master;

import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.protocol.modbus.ModbusPayloadInboundHandler;
import com.wincom.protocol.modbus.ReadMultipleHoldingRegistersResponse;
import com.wincom.protocol.modbus.WriteMultipleHoldingRegistersResponse;
import com.wincom.protocol.modbus.WriteSingleHoldingRegisterResponse;

/**
 *
 * @author master
 */
public class MasterPayloadInboundHandlerImpl
        extends ChannelInboundHandler.Adapter
        implements ModbusPayloadInboundHandler {

    @Override
    public void handleReadMultipleHoldingRegistersResponse(HandlerContext ctx, ReadMultipleHoldingRegistersResponse m) {
        ctx.fireInboundHandlerContexts(m);
        ctx.onRequestCompleted(m);
    }

    @Override
    public void handleWriteMultipleHoldingRegistersResponse(HandlerContext ctx, WriteMultipleHoldingRegistersResponse m) {
        ctx.fireInboundHandlerContexts(m);
        ctx.onRequestCompleted(m);
    }

    @Override
    public void handleWriteSingleHoldingRegisterResponse(HandlerContext ctx, WriteSingleHoldingRegisterResponse m) {
        ctx.fireInboundHandlerContexts(m);
        ctx.onRequestCompleted(m);
    }

    @Override
    public void handlePayloadSent(HandlerContext ctx, Message m) {
    }
}
