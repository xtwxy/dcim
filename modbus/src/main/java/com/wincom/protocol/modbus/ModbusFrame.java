package com.wincom.protocol.modbus;

import com.google.common.primitives.UnsignedBytes;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import static com.wincom.protocol.modbus.AbstractWireable.appendHeader;
import com.wincom.protocol.modbus.internal.CRC;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author master
 */
public class ModbusFrame
        extends AbstractWireable
        implements Wireable, Message {

    private byte slaveAddress;
    private ModbusFunction function;
    private ModbusPayload payload;
    short crc16;

    private transient boolean slave;

    public ModbusFrame() {
        slave = true;
    }

    public ModbusFrame(boolean slave) {
        this.slave = slave;
    }
    
    @Override
    public int getWireLength() {
        return 1 // slave address
                + 1 // modbus function
                + payload.getWireLength()
                + 2 // crc16
                ;
    }

    @Override
    public void toWire(ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);

        buffer.put(slaveAddress);
        buffer.put(function.getCode());
        payload.toWire(buffer);
        crc16 = CRC.crc16(buffer.array(), 0, getWireLength() - 2);
        buffer.putShort(crc16);
    }

    @Override
    public void fromWire(ByteBuffer buffer) {
        buffer.order(ByteOrder.BIG_ENDIAN);
        slaveAddress = buffer.get();
        function = ModbusFunction.from(buffer.get());

        payload = function.createResponse();
        payload.fromWire(buffer);

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

        appendChild(buf, depth, "Payload", payload);

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
        this.payload = isSlave() ? function.createResponse() : function.createRequest();
    }

    public ModbusPayload getPayload() {
        return payload;
    }

    public void setPayload(ModbusPayload payload) {
        this.payload = payload;
        this.function = payload.getFunctionCode();
    }

    public boolean isSlave() {
        return slave;
    }

    public void setSlave(boolean slave) {
        this.slave = slave;
    }

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        handler.handle(ctx, this);
    }

    @Override
    public boolean isOob() {
        return false;
    }

}
