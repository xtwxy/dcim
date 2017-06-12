package com.wincom.driver.dds3366d.internal;

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
public class PayloadInboundHandlerImpl
        extends ChannelInboundHandler.Adapter
        implements ModbusPayloadInboundHandler {

    @Override
    public void handleReadMultipleHoldingRegistersResponse(HandlerContext ctx, ReadMultipleHoldingRegistersResponse m) {
        handleModbusResponse(ctx, m);
    }

    @Override
    public void handleWriteMultipleHoldingRegistersResponse(HandlerContext ctx, WriteMultipleHoldingRegistersResponse m) {
        handleModbusResponse(ctx, m);
    }

    @Override
    public void handleWriteSingleHoldingRegisterResponse(HandlerContext ctx, WriteSingleHoldingRegisterResponse m) {
        handleModbusResponse(ctx, m);
    }

    @Override
    public void handlePayloadReceived(HandlerContext ctx, Message m) {
        handleModbusResponse(ctx, m);
    }

    @Override
    public void handlePayloadSent(HandlerContext ctx, Message m) {
        handleModbusResponse(ctx, m);
    }

    private void handleModbusResponse(HandlerContext ctx, Message m) {
        if (ctx.state().stopped()) {
            ctx.fireInboundHandlerContexts(m);
        } else {
            ctx.state().on(ctx, m);
        }
    }
}
