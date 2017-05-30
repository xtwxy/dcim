package com.wincom.protocol.modbus;

import com.wincom.dcim.agentd.primitives.HandlerContext;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author master
 */
public class WriteSingleHoldingRegisterRequest
        extends AbstractModbusRequest
        implements ModbusPayload {

    private short startAddress;
    private short valueToWrite;

    public WriteSingleHoldingRegisterRequest() {
    }

    @Override
    public ModbusFunction getFunctionCode() {
        return ModbusFunction.WRITE_SINGLE_HOLDING_REGISTER;
    }
    
    @Override
    public int getWireLength() {
        return 2 // start address of registers
                + 2 // value to write
                ;
    }

    @Override
    public void toWire(ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putShort(startAddress);
        buffer.putShort(valueToWrite);
    }

    @Override
    public void fromWire(ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        startAddress = buffer.getShort();
        valueToWrite = buffer.getShort();
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        appendHeader(buf, depth, getClass().getSimpleName());
        depth++;
        appendValue(buf, depth, "Start of Register Address", "0x" + Integer.toHexString(0xffff & startAddress));
        appendValue(buf, depth, "Value to Write", "0x" + Integer.toHexString(0xffff & valueToWrite));
    }

    public short getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(short startAddress) {
        this.startAddress = startAddress;
    }

    public short getValueToWrite() {
        return valueToWrite;
    }

    public void setValueToWrite(short valueToWrite) {
        this.valueToWrite = valueToWrite;
    }

    @Override
    public void applyModbusRequest(HandlerContext ctx, ModbusPayloadOutboundHandler handler) {
        handler.handleWriteSingleHoldingRegisterRequest(ctx, this);
    }
}
