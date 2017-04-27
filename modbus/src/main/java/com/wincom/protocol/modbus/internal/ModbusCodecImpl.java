package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.Dependency;
import com.wincom.dcim.agentd.IoCompletionHandler;
import com.wincom.protocol.modbus.ModbusFrame;
import io.netty.buffer.ByteBuf;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Composition of TCP connections to a MP3000.
 *
 * @author master
 */
public class ModbusCodecImpl extends Codec.Adapter implements Dependency {

    private final Map<Integer, ModbusCodecChannelImpl> outbound;
    private AgentdService agent;

    private ConcurrentLinkedDeque<Runnable> queue;
    private ByteBuffer readBuffer;
    ModbusFrame request;

    /**
     *
     * @param agent reference to agent service.
     */
    public ModbusCodecImpl(
            AgentdService agent
    ) {
        this.outbound = new HashMap<>();
        this.queue = new ConcurrentLinkedDeque<>();
        this.readBuffer = ByteBuffer.allocate(2048);
    }

    @Override
    public void encode(Object msg, IoCompletionHandler handler) {

        request = (ModbusFrame) msg;
        ByteBuffer buffer = ByteBuffer.allocate(request.getWireLength());
        request.toWire(buffer);
        final IoCompletionHandler writeCompletionHandler = new IoCompletionHandler.Adapter(handler) {
            @Override
            public void onComplete() {
                Runnable r = queue.poll();
                if (r != null) {
                    r = queue.peek();
                    if (r != null) {
                        getInbound().execute(r);
                    }
                }
                handler.onComplete();
            }
        };

        synchronized (queue) {
            boolean isFirst = false;
            /* to avoid of using if(queue.size() == 1) test. */
            if (queue.isEmpty()) {
                isFirst = true;
            }

            queue.add(new Runnable() {
                @Override
                public void run() {
                    getInbound().write(buffer, writeCompletionHandler);
                }
            });

            if (isFirst) {
                Runnable r = queue.peek();
                getInbound().execute(r);
            }
        }
    }

    @Override
    public void decode(Object msg) {
        receive(msg);
        if (!locateModbusAddress()) {
            return;
        }
        
        switch (request.getFunction()) {
            case READ_COILS:
                break;
            case READ_DISCRETE_INPUTS:
                break;
            case READ_MULTIPLE_HOLDING_REGISTERS:
                break;
            case READ_INPUT_REGISTERS:
                break;
            case WRITE_SINGLE_COIL:
                break;
            case WRITE_MULTIPLE_HOLDING_COILS:
                break;
            case WRITE_SINGLE_HOLDING_REGISTER:
            case WRITE_MULTIPLE_HOLDING_REGISTERS:
                if(readBuffer.remaining() >= 8) {
                    byte[] dst = new byte[8];
                    byte[] src = readBuffer.array();
                    System.arraycopy(src, 0, dst, 0, dst.length);
                    ByteBuffer buf = ByteBuffer.wrap(dst);
                    ModbusFrame frame = new ModbusFrame();
                    try {
                        frame.fromWire(buf);
                    } catch (Throwable t) {
                        // TODO: error handling.
                    }
                    // TODO: complete request/response cycle.
                    readBuffer.position(dst.length);
                    readBuffer.compact();
                }
                break;
        }
    }

    private boolean locateModbusAddress() {
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
        if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;
            readBuffer.put(buf.nioBuffer());
            buf.release();
        } else {
            throw new IllegalArgumentException("Not a ByteBuf: " + msg);
        }
    }

    /**
     * Connect <code>Codec</code> to <code>Codecchannel</code> with identifier
     * <code>channelId</code>.
     *
     * @param channelId the identifier of the <code>CodecChannel</code>.
     * @param cc the <code>Codec</code> to be connected.
     */
    @Override
    public void setOutboundCodec(String channelId, Codec cc) {
        int address = Integer.parseInt(channelId);
        ModbusCodecChannelImpl codecChannel = new ModbusCodecChannelImpl(address, agent);
        codecChannel.setInboundCodec(this);
        codecChannel.setOutboundCodec(cc);
        outbound.put(address, codecChannel);
    }

    @Override
    public Runnable withDependencies(Runnable r) {
        return getInbound().withDependencies(r);
    }
}
