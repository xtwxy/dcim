package com.wincom.protocol.modbus;

import com.wincom.dcim.agentd.messages.AbstractWireable;
import com.wincom.dcim.agentd.messages.Wireable;
import com.google.common.primitives.UnsignedBytes;
import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.ChannelOutboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.Handler;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author master
 */
public class ModbusFrame
        extends AbstractWireable
        implements Wireable {

    private byte slaveAddress;
    private ModbusFunction function;
    private ModbusPayload payload;
    private short crc16;

    private transient boolean request;

    public ModbusFrame(HandlerContext sender) {
        super(sender);
        request = true;
    }

    public ModbusFrame(HandlerContext sender, boolean request) {
        super(sender);
        this.request = request;
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

        payload = isRequest() ? function.createRequest(getSender()) : function.createResponse(getSender());
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
    public void apply(HandlerContext ctx, Handler handler) {
        if(isRequest()) {
            if(handler instanceof ChannelOutboundHandler) {
                ((ChannelOutboundHandler)handler).handleSendPayload(ctx, this);
                return;
            }
        } else {
            if(handler instanceof ChannelInboundHandler) {
                ((ChannelInboundHandler)handler).handlePayloadReceived(ctx, this);
                return;
            }
        }
        handler.handle(ctx, this);
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
        this.payload = isRequest() ? function.createRequest(getSender()) : function.createResponse(getSender());
    }

    public ModbusPayload getPayload() {
        return payload;
    }

    public void setPayload(ModbusPayload payload) {
        this.payload = payload;
        this.function = payload.getFunctionCode();
    }

    private boolean isRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }

}
