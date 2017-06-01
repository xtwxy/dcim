package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.Failed;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.protocol.modbus.ModbusPayloadInboundHandler;
import com.wincom.protocol.modbus.ReadMultipleHoldingRegistersResponse;
import com.wincom.protocol.modbus.WriteMultipleHoldingRegistersResponse;
import com.wincom.protocol.modbus.WriteSingleHoldingRegisterResponse;

/**
 *
 * @author master
 */
public class ModbusPayloadInboundHandlerImpl
        extends ModbusPayloadInboundHandler.Adapter
        implements ModbusPayloadInboundHandler {

    @Override
    public void handleChannelInactive(HandlerContext ctx, ChannelInactive m) {
        ctx.onClosed(m);
    }

    @Override
    public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m) {
        ctx.onRequestCompleted(m);
    }

    @Override
    public void handleReadMultipleHoldingRegistersResponse(HandlerContext ctx, ReadMultipleHoldingRegistersResponse m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleWriteMultipleHoldingRegistersResponse(HandlerContext ctx, WriteMultipleHoldingRegistersResponse m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleWriteSingleHoldingRegisterResponse(HandlerContext ctx, WriteSingleHoldingRegisterResponse m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handlePayloadSent(HandlerContext ctx, Message m) {
    }

    @Override
    public void handleFailed(HandlerContext ctx, Failed m) {
    }
}
