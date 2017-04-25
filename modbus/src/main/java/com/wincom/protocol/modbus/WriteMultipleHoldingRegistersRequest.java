package com.wincom.protocol.modbus;

import com.google.common.primitives.UnsignedBytes;
import com.wincom.protocol.modbus.internal.CRC;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author master
 */
public class WriteMultipleHoldingRegistersRequest
        extends AbstractWireable
        implements Wireable {

    private byte slaveAddress;
    private ModbusFunction function;
    private short startAddress;
    private short numberOfRegisters;
    private byte numberOfBytes;
    private short[] registers;
    short crc16;


    public WriteMultipleHoldingRegistersRequest() {
        function = ModbusFunction.WRITE_MULTIPLE_HOLDING_REGISTERS;
    }

    public WriteMultipleHoldingRegistersRequest(byte numberOfRegisters) {
        this();
        registers = new short[numberOfRegisters];
        this.numberOfRegisters = numberOfRegisters;
        this.numberOfBytes = (byte)((2 * numberOfRegisters) & 0xff);
    }
    
    @Override
    public int getWireLength() {
        return 1 // modbus slave address
                + 1 // modbus command
                + 2 // start address
                + 2 // number of registers
                + 1 // number of bytes
                + 2 * registers.length // byte content
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
        buffer.put(numberOfBytes);
        for(short r : registers) {
            buffer.putShort(r);
        }
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
        numberOfBytes = buffer.get();
        for(int i = 0; i < registers.length; ++i) {
            registers[i] = buffer.getShort();
        }
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
        appendValue(buf, depth, "Slave Address", "0x" + String.format("0x%02x", slaveAddress, 16));
        appendValue(buf, depth, "Function Code", "0x" + UnsignedBytes.toString(function.getCode(), 16));
        appendValue(buf, depth, "Start of Register Address", "0x" + Integer.toHexString(0xffff & startAddress));
        appendValue(buf, depth, "Number of Registers", "0x" + Integer.toHexString(0xffff & numberOfRegisters));
        appendValue(buf, depth, "Number of Bytes", "0x" + UnsignedBytes.toString(numberOfBytes, 16));
        appendValue(buf, depth, "Registers", registers);
        appendValue(buf, depth, "CRC16", "0x" + Integer.toHexString(0xffff & crc16));
    }

    public byte getSlaveAddress() {
        return slaveAddress;
    }

    public void setSlaveAddress(byte slaveAddress) {
        this.slaveAddress = slaveAddress;
    }

    public ModbusFunction getFunction() {
        return function;
    }

    public void setFunction(ModbusFunction function) {
        this.function = function;
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
        registers = new short[numberOfRegisters];
        this.numberOfRegisters = numberOfRegisters;
        this.numberOfBytes = (byte)((2 * numberOfRegisters) & 0xff);
    }

    public short[] getRegisters() {
        return registers;
    }

    public void setRegisters(short[] registers) {
        this.registers = registers;
    }

}
