package com.wincom.protocol.modbus;

import com.google.common.primitives.UnsignedBytes;
import com.wincom.protocol.modbus.internal.CRC;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author master
 */
public class ReadMultipleHoldingRegistersRequest
        extends AbstractWireable
        implements Wireable {

    private byte slaveAddress;
    private ModbusFunction function;
    private short startAddress;
    private short numberOfRegisters;
    short crc16;

    public ReadMultipleHoldingRegistersRequest() {
        function = ModbusFunction.READ_MULTIPLE_HOLDING_REGISTERS;
    }
    
    @Override
    public int getWireLength() {
        return 1 // modbus slave address
                + 1 // modbus command
                + 2 // start address of registers
                + 2 // number of registers
                + 2 // CRC16
                ;
    }

    @Override
    public void toWire(ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(slaveAddress);
        buffer.put(function.getCode());
        buffer.putShort(startAddress);
        buffer.putShort(numberOfRegisters);
        crc16 = CRC.crc16(buffer.array(), 0, getWireLength() - 2);
        buffer.putShort(crc16);
    }

    @Override
    public void fromWire(ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        slaveAddress = buffer.get();
        function = ModbusFunction.from(buffer.get());
        startAddress = buffer.getShort();
        numberOfRegisters = buffer.getShort();
        crc16 = buffer.getShort();
        short calculated = CRC.crc16(buffer.array(), 0, getWireLength() - 2);
        if (crc16 != calculated) {
            throw new IllegalArgumentException(
                    "CRC16 does not match: crc16 = " + crc16
                    + ", CRC.crc16(buffer) = " + calculated
            );
        } else {
            crc16 = calculated;
        }
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        appendHeader(buf, depth, getClass().getSimpleName());
        depth++;
        appendValue(buf, depth, "Slave Address", "0x" + UnsignedBytes.toString(slaveAddress, 16));
        appendValue(buf, depth, "Function Code", "0x" + UnsignedBytes.toString(function.getCode(), 16));
        appendValue(buf, depth, "Start of Register Address", "0x" + Integer.toHexString(0xffff & startAddress));
        appendValue(buf, depth, "Number of Registers", "0x" + Integer.toHexString(0xffff & numberOfRegisters));
        appendValue(buf, depth, "CRC16", "0x" + Integer.toHexString(0xffff & crc16));
    }

    public byte getSlaveAddress() {
        return slaveAddress;
    }

    public void setSlaveAddress(byte slaveAddress) {
        this.slaveAddress = slaveAddress;
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
