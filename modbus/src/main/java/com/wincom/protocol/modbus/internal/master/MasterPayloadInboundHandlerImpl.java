package com.wincom.protocol.modbus.internal.master;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.ApplicationFailure;
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
        extends ModbusPayloadInboundHandler.Adapter
        implements ModbusPayloadInboundHandler {

    @Override
    public void handleChannelInactive(HandlerContext ctx, ChannelInactive m) {
        super.handleChannelInactive(ctx, m);
    }

    @Override
    public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m) {
        super.handleChannelTimeout(ctx, m);
    }

    @Override
    public void handleReadMultipleHoldingRegistersResponse(HandlerContext ctx, ReadMultipleHoldingRegistersResponse m) {
    }

    @Override
    public void handleWriteMultipleHoldingRegistersResponse(HandlerContext ctx, WriteMultipleHoldingRegistersResponse m) {
    }

    @Override
    public void handleWriteSingleHoldingRegisterResponse(HandlerContext ctx, WriteSingleHoldingRegisterResponse m) {
    }

    @Override
    public void handlePayloadSent(HandlerContext ctx, Message m) {
    }
}
