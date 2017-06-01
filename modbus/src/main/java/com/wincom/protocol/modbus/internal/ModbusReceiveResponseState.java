package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.Failed;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.protocol.modbus.ModbusFrame;
import java.nio.ByteBuffer;

/**
 *
 * @author master
 */
public class ModbusReceiveResponseState extends State.Adapter {

    private ByteBuffer readBuffer;
    private ModbusFrame request;

    final private HandlerContext replyTo;

    ModbusReceiveResponseState(HandlerContext replyTo) {
        this.replyTo = replyTo;
    }

    @Override
    public State enter(HandlerContext ctx) {
        readBuffer = (ByteBuffer) ctx.getOrSetIfNotExist(ModbusCodecImpl.READ_BUFFER_KEY, ByteBuffer.allocate(2048));
        request = (ModbusFrame) ctx.get(ModbusCodecImpl.MODBUS_REQUEST_KEY);
        if (request == null) {
            return error();
        }
        // encode and send.
        return this;
    }

    @Override
    public State on(HandlerContext ctx, Message m) {

        if (m instanceof BytesReceived) {
            decode(ctx, readBuffer);
        }
        return success();
    }

    private void decode(HandlerContext ctx, ByteBuffer readBuffer) {
        if (request == null) {
            readBuffer.clear();
            return;
        }
        if (!locateModbusAddress(request)) {
            return;
        }

        byte[] src = null;
        switch (request.getFunction()) {
            case READ_COILS:
                break;
            case READ_DISCRETE_INPUTS:
                break;
            case READ_MULTIPLE_HOLDING_REGISTERS:
                if (readBuffer.remaining() < 3) {
                    break;
                }

                src = readBuffer.array();
                final int LENGTH = 5 + src[2];
                if (readBuffer.remaining() < LENGTH) {
                    break;
                }

                decode(ctx, ByteBuffer.wrap(src, 0, LENGTH), request);

                break;
            case READ_INPUT_REGISTERS:
                break;
            case WRITE_SINGLE_COIL:
                break;
            case WRITE_MULTIPLE_HOLDING_COILS:
                break;
            case WRITE_SINGLE_HOLDING_REGISTER:
            case WRITE_MULTIPLE_HOLDING_REGISTERS:
                if (readBuffer.remaining() < 8) {
                    break;
                }

                src = readBuffer.array();
                decode(ctx, ByteBuffer.wrap(src, 0, 8), request);
                break;
            default:
                // TODO: handle default case.
                break;
        }
    }

    private void decode(HandlerContext ctx, ByteBuffer buf, ModbusFrame request) {
        final int LENGTH = buf.remaining();
        ModbusFrame response = new ModbusFrame();
        Message result = null;
        try {
            response.fromWire(buf);
            if (request.getSlaveAddress() == response.getSlaveAddress()
                    && request.getFunction() == response.getFunction()) {
                result = response;
            } else {
                final String error = String.format("request(address = %s, function = %s) != response(address = %s, function = %s)",
                        request.getSlaveAddress(), request.getFunction(),
                        response.getSlaveAddress(), response.getFunction());

                result = new Failed(ctx, new Exception(error));
            }
        } catch (Exception e) {
            result = new Failed(ctx, e);
        }
        readBuffer.position(LENGTH);
        readBuffer.compact();
        replyTo.fire(result);
        ctx.onRequestCompleted(result);
    }

    private boolean locateModbusAddress(ModbusFrame request) {
        boolean addressLocated = false;
        do {
            byte[] bytes = readBuffer.array();
            if (request.getSlaveAddress() != bytes[0]
                    || request.getFunction().getCode() != bytes[1]) {
                readBuffer.position(1);
                readBuffer.compact();
                readBuffer.flip();
            } else {
                addressLocated = true;
                break;
            }
        } while (readBuffer.hasRemaining());
        return addressLocated;
    }
}
