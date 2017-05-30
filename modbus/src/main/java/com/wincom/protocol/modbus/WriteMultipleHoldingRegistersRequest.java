package com.wincom.protocol.modbus;

import com.google.common.primitives.UnsignedBytes;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author master
 */
public class WriteMultipleHoldingRegistersRequest
        extends AbstractModbusRequest
        implements ModbusPayload {

    private short startAddress;
    private short numberOfRegisters;
    private byte numberOfBytes;
    private short[] registers;


    public WriteMultipleHoldingRegistersRequest() {
    }

    public WriteMultipleHoldingRegistersRequest(byte numberOfRegisters) {
        this();
        registers = new short[numberOfRegisters];
        this.numberOfRegisters = numberOfRegisters;
        this.numberOfBytes = (byte)((2 * numberOfRegisters) & 0xff);
    }
    
    @Override
    public ModbusFunction getFunctionCode() {
        return ModbusFunction.WRITE_MULTIPLE_HOLDING_REGISTERS;
    }
    
    @Override
    public int getWireLength() {
        return 2 // start address
                + 2 // number of registers
                + 1 // number of bytes
                + 2 * registers.length // byte content
                ;
    }

    @Override
    public void toWire(ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putShort(startAddress);
        buffer.putShort(numberOfRegisters);
        buffer.put(numberOfBytes);
        for(short r : registers) {
            buffer.putShort(r);
        }
    }

    @Override
    public void fromWire(ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        startAddress = buffer.getShort();
        numberOfRegisters = buffer.getShort();
        numberOfBytes = buffer.get();
        for(int i = 0; i < registers.length; ++i) {
            registers[i] = buffer.getShort();
        }
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        appendHeader(buf, depth, getClass().getSimpleName());
        depth++;
        appendValue(buf, depth, "Start of Register Address", "0x" + Integer.toHexString(0xffff & startAddress));
        appendValue(buf, depth, "Number of Registers", "0x" + Integer.toHexString(0xffff & numberOfRegisters));
        appendValue(buf, depth, "Number of Bytes", "0x" + UnsignedBytes.toString(numberOfBytes, 16));
        appendValue(buf, depth, "Registers", registers);
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

    @Override
    public void applyModbusRequest(HandlerContext ctx, ModbusPayloadOutboundHandler handler) {
        handler.handleWriteMultipleHoldingRegistersRequest(ctx, this);
    }
}
