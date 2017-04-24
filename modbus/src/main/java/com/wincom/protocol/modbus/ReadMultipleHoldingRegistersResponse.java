package com.wincom.protocol.modbus;

import com.google.common.primitives.UnsignedBytes;
import com.wincom.protocol.modbus.internal.CRC;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author master
 */
public class ReadMultipleHoldingRegistersResponse
        extends AbstractWireable
        implements Wireable {

    private byte slaveAddress;
    private ModbusFunction function;
    private byte numberOfBytes;
    private byte[] bytes;
    short crc16;


    public ReadMultipleHoldingRegistersResponse() {
        function = ModbusFunction.READ_MULTIPLE_HOLDING_REGISTERS;
    }

    public ReadMultipleHoldingRegistersResponse(byte numberOfBytes) {
        this();
        bytes = new byte[numberOfBytes];
        this.numberOfBytes = numberOfBytes;
    }
    
    @Override
    public int getWireLength() {
        return 1 // modbus slave address
                + 1 // modbus command
                + 1 // number of bytes
                + bytes.length // byte content
                + 2 // CRC16
                ;
    }

    @Override
    public void toWire(ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(slaveAddress);
        buffer.put(function.getCode());
        buffer.put(numberOfBytes);
        buffer.put(bytes);
        crc16 = CRC.crc16(buffer.array(), 0, getWireLength() - 2);
        buffer.putShort(crc16);
    }

    @Override
    public void fromWire(ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        slaveAddress = buffer.get();
        function = ModbusFunction.from(buffer.get());
        numberOfBytes = buffer.get();
        buffer.get(bytes);
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
        appendValue(buf, depth, "Number of Bytes", "0x" + UnsignedBytes.toString(numberOfBytes, 16));
        appendValue(buf, depth, "Bytes", bytes);
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

    public short getNumberOfBytes() {
        return numberOfBytes;
    }

    public void setNumberOfBytes(byte numberOfBytes) {
        bytes = new byte[numberOfBytes];
        this.numberOfBytes = numberOfBytes;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

}
