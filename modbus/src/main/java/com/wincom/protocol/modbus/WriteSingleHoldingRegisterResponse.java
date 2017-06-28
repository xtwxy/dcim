package com.wincom.protocol.modbus;

import com.sun.istack.NotNull;
import com.wincom.dcim.agentd.HandlerContext;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author master
 */
public class WriteSingleHoldingRegisterResponse
        extends AbstractModbusResponse
        implements ModbusPayload {

    private short startAddress;
    private short valueWritten;

    public WriteSingleHoldingRegisterResponse(HandlerContext sender) {
        super(sender);
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
    public void toWire(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putShort(startAddress);
        buffer.putShort(valueWritten);
    }

    @Override
    public void fromWire(@NotNull ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        startAddress = buffer.getShort();
        valueWritten = buffer.getShort();
    }

    @Override
    public void toStringBuilder(@NotNull StringBuilder buf, int depth) {
        appendHeader(buf, depth, getClass().getSimpleName());
        depth++;
        appendValue(buf, depth, "Start of Register Address", "0x" + Integer.toHexString(0xffff & startAddress));
        appendValue(buf, depth, "Value Written", "0x" + Integer.toHexString(0xffff & valueWritten));
    }

    public short getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(short startAddress) {
        this.startAddress = startAddress;
    }

    public short getValueWritten() {
        return valueWritten;
    }

    public void setValueWritten(short valueWritten) {
        this.valueWritten = valueWritten;
    }

    @Override
    public void applyModbusResponse(HandlerContext ctx, ModbusPayloadInboundHandler handler) {
        handler.handleWriteSingleHoldingRegisterResponse(ctx, this);
    }
}
