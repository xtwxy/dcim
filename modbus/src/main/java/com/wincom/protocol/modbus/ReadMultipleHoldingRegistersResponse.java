package com.wincom.protocol.modbus;

import com.google.common.primitives.UnsignedBytes;
import com.wincom.dcim.agentd.HandlerContext;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author master
 */
public class ReadMultipleHoldingRegistersResponse
        extends AbstractModbusResponse
        implements ModbusPayload {

    private byte numberOfBytes;
    private byte[] bytes;

    public ReadMultipleHoldingRegistersResponse(HandlerContext sender) {
        super(sender);
    }

    public ReadMultipleHoldingRegistersResponse(HandlerContext sender, byte numberOfBytes) {
        super(sender);
        bytes = new byte[numberOfBytes];
        this.numberOfBytes = numberOfBytes;
    }
    
    @Override
    public ModbusFunction getFunctionCode() {
        return ModbusFunction.READ_MULTIPLE_HOLDING_REGISTERS;
    }
    
    @Override
    public int getWireLength() {
        return 1 // number of bytes
                + bytes.length // byte content
                ;
    }

    @Override
    public void toWire(ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(numberOfBytes);
        buffer.put(bytes);
    }

    @Override
    public void fromWire(ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        numberOfBytes = buffer.get();
        setNumberOfBytes(numberOfBytes);
        buffer.get(bytes);
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        appendHeader(buf, depth, getClass().getSimpleName());
        depth++;
        appendValue(buf, depth, "Number of Bytes", "0x" + UnsignedBytes.toString(numberOfBytes, 16));
        appendValue(buf, depth, "Bytes", bytes);
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

    @Override
    public void applyModbusResponse(HandlerContext ctx, ModbusPayloadInboundHandler handler) {
        handler.handleReadMultipleHoldingRegistersResponse(ctx, this);
    }
}
