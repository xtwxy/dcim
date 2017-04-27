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

    private final Map<Byte, ModbusCodecChannelImpl> outbound;
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

        final ModbusFrame frame = (ModbusFrame) msg;
        ByteBuffer buffer = ByteBuffer.allocate(frame.getWireLength());
        frame.toWire(buffer);

        synchronized (queue) {
            boolean isFirst = false;
            /* to avoid of using if(queue.size() == 1) test. */
            if (queue.isEmpty()) {
                isFirst = true;
            }

            queue.add(new Runnable() {
                @Override
                public void run() {
                    request = frame;
                    getInbound().write(buffer, handler);
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
        
        byte[] src = null;
        byte[] dst = null;
        switch (request.getFunction()) {
            case READ_COILS:
                break;
            case READ_DISCRETE_INPUTS:
                break;
            case READ_MULTIPLE_HOLDING_REGISTERS:
                if(readBuffer.remaining() < 3) break;

                src = readBuffer.array();
                final int LENGTH = 5 + src[2];
                if(readBuffer.remaining() < LENGTH) break;
                dst = new byte[LENGTH];
                decode(src, dst);
                
                break;
            case READ_INPUT_REGISTERS:
                break;
            case WRITE_SINGLE_COIL:
                break;
            case WRITE_MULTIPLE_HOLDING_COILS:
                break;
            case WRITE_SINGLE_HOLDING_REGISTER:
            case WRITE_MULTIPLE_HOLDING_REGISTERS:
                if(readBuffer.remaining() < 8) break;

                dst = new byte[8];
                src = readBuffer.array();
                decode(src, dst);
                break;
        }
    }

    private void decode(byte[] src, byte[] dst) {
        System.arraycopy(src, 0, dst, 0, dst.length);
        ByteBuffer buf = ByteBuffer.wrap(dst);
        ModbusFrame frame = new ModbusFrame();
        try {
            frame.fromWire(buf);
        } catch (Exception e) {
            outbound.get(request.getSlaveAddress()).fireError(e);
            readBuffer.position(dst.length);
            readBuffer.compact();
            dequeue();
            return;
        }
        outbound.get(request.getSlaveAddress()).fireComplete();
        readBuffer.position(dst.length);
        readBuffer.compact();
        dequeue();
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
        } else if (msg instanceof ByteBuffer) {
            ByteBuffer buf = (ByteBuffer) msg;
            readBuffer.put(buf);
        } else {
            throw new IllegalArgumentException("Not a ByteBuf or ByteBuffer: " + msg);
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
        outbound.put((byte)(0xff & address), codecChannel);
    }

    @Override
    public Runnable withDependencies(Runnable r) {
        return getInbound().withDependencies(r);
    }
    
    private void dequeue() {
        synchronized (queue) {
            queue.poll();
            Runnable r = queue.peek();
            if(r != null) {
                getInbound().execute(r);
            }
        }
    }
}
