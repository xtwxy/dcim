package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.ChannelOutboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.protocol.modbus.ModbusPayload;
import com.wincom.protocol.modbus.ModbusPayloadOutboundHandler;
import com.wincom.protocol.modbus.ReadMultipleHoldingRegistersRequest;
import com.wincom.protocol.modbus.WriteMultipleHoldingRegistersRequest;
import com.wincom.protocol.modbus.WriteSingleHoldingRegisterRequest;

/**
 *
 * @author master
 */
public class PayloadOutboundHandlerImpl
        extends ChannelOutboundHandler.Adapter
        implements ModbusPayloadOutboundHandler {

    PayloadOutboundHandlerImpl() {
    }

    @Override
    public void handleReadMultipleHoldingRegistersRequest(HandlerContext ctx, ReadMultipleHoldingRegistersRequest m) {
        handleSendModbusPayload(m);
    }

    @Override
    public void handleWriteMultipleHoldingRegistersRequest(HandlerContext ctx, WriteMultipleHoldingRegistersRequest m) {
        handleSendModbusPayload(m);
    }

    @Override
    public void handleWriteSingleHoldingRegisterRequest(HandlerContext ctx, WriteSingleHoldingRegisterRequest m) {
        handleSendModbusPayload(m);
    }

    public void handleSendModbusPayload(ModbusPayload m) {
    }

    @Override
    public void handleSendPayload(HandlerContext ctx, Message m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
