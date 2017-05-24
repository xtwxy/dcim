package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.Failed;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SendBytes;
import com.wincom.dcim.agentd.statemachine.ReceiveState;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import com.wincom.dcim.agentd.statemachine.StateMachineBuilder;
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

    private HandlerContext outboundContext;
    private final Map<Byte, HandlerContext> inboundContexts;

    private final ByteBuffer readBuffer;

    public ModbusCodecImpl() {
        this.inboundContexts = new HashMap<>();
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
            inboundContexts.get(request.getSlaveAddress()).onSendComplete(new Failed(e));
            readBuffer.position(dst.length);
            readBuffer.compact();
            return;
        }
        if(request.getSlaveAddress() == response.getSlaveAddress()
                && request.getFunction() == response.getFunction()) {
            inboundContexts.get(request.getSlaveAddress()).onSendComplete(response.getPayload());
        } else {
            final String error = String.format("request(address = %s, function = %s) != response(address = %s, function = %s)", 
                    request.getSlaveAddress(), request.getFunction(),
                    response.getSlaveAddress(), response.getFunction());
            
            inboundContexts.get(request.getSlaveAddress())
                    .onSendComplete(new Failed(new Exception(error)));
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
            throw new IllegalArgumentException("Not a ByteBuf or ByteBuffer: " + msg);
        }
    }

    @Override
    public void codecActive(HandlerContext outboundContext) {
        this.outboundContext = outboundContext;
        for (Map.Entry<Byte, HandlerContext> e : inboundContexts.entrySet()) {
            e.getValue().initHandlers(outboundContext);
        }
    }

    @Override
    public HandlerContext openInbound(
            AgentdService service,
            Properties props,
            Handler inboundHandler) {
        log.info(String.format("%s", props));

        Byte address = Byte.valueOf(props.getProperty("address"));
        // FIXME: add address validation.
        HandlerContext inboundContext = inboundContexts.get(address);
        if (inboundContext == null) {
            inboundContext = createInbound0(address, inboundHandler);

            inboundContexts.put(address, inboundContext);
        }

        return inboundContext;
    }

    private HandlerContext createInbound0(
            final Byte address,
            final Handler inboundHandler) {

        final HandlerContext handlerContext = new ModbusHandlerContextImpl() {
            @Override
            public void close() {
                inboundContexts.remove(address);
            }
        };
        
        handlerContext.setInboundHandler(inboundHandler);
        
        final StateMachineBuilder builder = new StateMachineBuilder();

        StateMachine client = builder
                .add("receiveState", new ReceiveState())
                .transision("receiveState", "receiveState", "receiveState")
                .buildWithInitialState("receiveState");

        handlerContext.getStateMachine()
                .buildWith(client)
                .enter(handlerContext);
        
        handlerContext.initHandlers(this.outboundContext);
        
        return handlerContext;
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
