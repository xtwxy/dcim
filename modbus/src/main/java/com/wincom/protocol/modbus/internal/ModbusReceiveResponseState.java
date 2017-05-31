package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.Failed;
import com.wincom.dcim.agentd.primitives.HandlerContext;
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
    public static final String READ_BUFFER = "READ_BUFFER";

    ModbusReceiveResponseState(HandlerContext replyTo) {
        this.replyTo = replyTo;
    }
    
    @Override
    public State enter(HandlerContext ctx) {
        readBuffer = (ByteBuffer) ctx.getOrSetIfNotExist(READ_BUFFER, ByteBuffer.allocate(2048));
        request = (ModbusFrame) ctx.get(ModbusSendRequestState.MODBUS_REQUEST);
        if(request == null) {
            return error();
        }
        // encode and send.
        return this;
    }

    @Override
    public State on(HandlerContext ctx, Message m) {
        
        if (m instanceof BytesReceived) {
            // TODO: notify the inbound handlers.
            BytesReceived bm = (BytesReceived) m;
            decode(ctx, bm.getByteBuffer());
            return success();
        } else if (m instanceof ChannelTimeout) {
            ctx.onRequestCompleted(m);
            return success();
        } else if (m instanceof ChannelInactive) {
            ctx.onRequestCompleted(m);
            return error();
        } else {
            log.info(String.format("default: (%s, %s, %s), leave to the inbound handler.", this, ctx, m));
            ctx.getInboundHandler().handle(ctx, m);
            return success();
        }
    }

    public void decode(HandlerContext ctx, Object msg) {
        receive(msg);
        if (request == null) {
            readBuffer.clear();
            return;
        }
        if (!locateModbusAddress(request)) {
            return;
        }

        byte[] src = null;
        byte[] dst = null;
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
                dst = new byte[LENGTH];
                decode(ctx, src, dst, request);

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

                dst = new byte[8];
                src = readBuffer.array();
                decode(ctx, src, dst, request);
                break;
            default:
                // TODO: handle default case.
                break;
        }
    }

    private void decode(HandlerContext ctx, byte[] src, byte[] dst, ModbusFrame request) {
        System.arraycopy(src, 0, dst, 0, dst.length);
        ByteBuffer buf = ByteBuffer.wrap(dst);
        ModbusFrame response = new ModbusFrame();
        try {
            response.fromWire(buf);
        } catch (Exception e) {
            ctx.onRequestCompleted(new Failed(e));
            readBuffer.position(dst.length);
            readBuffer.compact();
            return;
        }
        if(request.getSlaveAddress() == response.getSlaveAddress()
                && request.getFunction() == response.getFunction()) {
            ctx.onRequestCompleted(response.getPayload());
        } else {
            final String error = String.format("request(address = %s, function = %s) != response(address = %s, function = %s)", 
                    request.getSlaveAddress(), request.getFunction(),
                    response.getSlaveAddress(), response.getFunction());
            
            ctx.onRequestCompleted(new Failed(new Exception(error)));
        }
        readBuffer.position(dst.length);
        readBuffer.compact();
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

    private void receive(Object msg) throws IllegalArgumentException {
        if (msg instanceof ByteBuffer) {
            ByteBuffer buf = (ByteBuffer) msg;
            readBuffer.put(buf);
        } else {
            log.error(String.format("Not a ByteBuf or ByteBuffer: %s", msg));
        }
    }

    @Override
    public String toString() {
        return "ModbusRequestState@" + this.hashCode();
    }
}
