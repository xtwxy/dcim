package com.wincom.protocol.modbus.internal.master;

import com.wincom.dcim.agentd.ChannelOutboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.protocol.modbus.ModbusFrame;
import com.wincom.protocol.modbus.ModbusPayload;
import com.wincom.protocol.modbus.ModbusPayloadOutboundHandler;
import com.wincom.protocol.modbus.ReadMultipleHoldingRegistersRequest;
import com.wincom.protocol.modbus.WriteMultipleHoldingRegistersRequest;
import com.wincom.protocol.modbus.WriteSingleHoldingRegisterRequest;

/**
 *
 * @author master
 */
public class MasterPayloadOutboundHandlerImpl
        extends ChannelOutboundHandler.Adapter
        implements ModbusPayloadOutboundHandler {

    private final byte slaveAddress;

    MasterPayloadOutboundHandlerImpl(byte slaveAddress) {
        this.slaveAddress = slaveAddress;
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
        ModbusFrame frame = new ModbusFrame();
        frame.setSlaveAddress(slaveAddress);
        frame.setPayload(m);
        outboundContext.send(m);
    }

    @Override
    public void handleSendPayload(HandlerContext ctx, Message m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
