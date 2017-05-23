package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.Failed;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SendBytes;
import com.wincom.protocol.modbus.ModbusFrame;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Composition of TCP connections to a MP3000.
 *
 * @author master
 */
public class ModbusCodecImpl implements Codec {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private final Map<Byte, HandlerContext> inbound;

    private final ByteBuffer readBuffer;

    public ModbusCodecImpl() {
        this.inbound = new HashMap<>();
        this.readBuffer = ByteBuffer.allocate(2048);
    }

    public void encode(HandlerContext ctx, Object msg, HandlerContext reply, ModbusFrame request) {
        final ModbusFrame frame = (ModbusFrame) msg;
        ByteBuffer buffer = ByteBuffer.allocate(frame.getWireLength());
        frame.toWire(buffer);

        ctx.send(new SendBytes(buffer), reply);
    }

    public void decode(HandlerContext ctx, Object msg) {
        receive(msg);
        ModbusFrame request = getRequest(ctx);
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
        }
    }

    private void decode(HandlerContext ctx, byte[] src, byte[] dst, ModbusFrame request) {
        System.arraycopy(src, 0, dst, 0, dst.length);
        ByteBuffer buf = ByteBuffer.wrap(dst);
        ModbusFrame frame = new ModbusFrame();
        try {
            frame.fromWire(buf);
        } catch (Exception e) {
            inbound.get(request.getSlaveAddress()).onSendComplete(new Failed(e));
            readBuffer.position(dst.length);
            readBuffer.compact();
            return;
        }
        inbound.get(request.getSlaveAddress()).onSendComplete(frame);
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
            throw new IllegalArgumentException("Not a ByteBuf or ByteBuffer: " + msg);
        }
    }

    @Override
    public void codecActive(HandlerContext outboundContext) {

    }

    @Override
    public HandlerContext openInbound(AgentdService service, Properties props, Handler inboundHandler) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Handle inbound events.
     *
     * @param ctx the outbound handler context.
     * @param m
     */
    @Override
    public void handle(HandlerContext ctx, Message m) {
        log.info(String.format("handle(%s, %s, %s)", this, ctx, m));
        if (m instanceof BytesReceived) {
            BytesReceived bm = (BytesReceived) m;
            decode(ctx, bm.getByteBuffer());
        }
    }

    private ModbusFrame getRequest(HandlerContext ctx) {
        return (ModbusFrame) ctx.getCurrentSendingMessage();
    }

}
