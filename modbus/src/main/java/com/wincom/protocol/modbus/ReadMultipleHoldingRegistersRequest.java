package com.wincom.protocol.modbus;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author master
 */
public class ReadMultipleHoldingRegistersRequest
        extends AbstractWireable
        implements ModbusPayload {

    private short startAddress;
    private short numberOfRegisters;

    public ReadMultipleHoldingRegistersRequest() {
    }
    
    @Override
    public ModbusFunction getFunctionCode() {
        return ModbusFunction.READ_MULTIPLE_HOLDING_REGISTERS;
    }
    
    @Override
    public int getWireLength() {
        return 2 // start address of registers
                + 2 // number of registers
                ;
    }

    @Override
    public void toWire(ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putShort(startAddress);
        buffer.putShort(numberOfRegisters);
    }

    @Override
    public void fromWire(ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        startAddress = buffer.getShort();
        numberOfRegisters = buffer.getShort();
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        appendHeader(buf, depth, getClass().getSimpleName());
        depth++;
        appendValue(buf, depth, "Start of Register Address", "0x" + Integer.toHexString(0xffff & startAddress));
        appendValue(buf, depth, "Number of Registers", "0x" + Integer.toHexString(0xffff & numberOfRegisters));
    }

    public short getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(short startAddress) {
        this.startAddress = startAddress;
    }

    public short getNumberOfRegisters() {
        return numberOfRegisters;
    }

    public void setNumberOfRegisters(short numberOfRegisters) {
        this.numberOfRegisters = numberOfRegisters;
    }

}
