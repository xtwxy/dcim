package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.CodecChannel;
import com.wincom.dcim.agentd.Dependency;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPromise;
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

    private CodecChannel inbound;
    private final Map<Integer, ModbusCodecChannelImpl> outbound;
    private AgentdService agent;
    
    private ConcurrentLinkedDeque<Runnable> queue;

    /**
     *
     * @param agent reference to agent service.
     */
    public ModbusCodecImpl(
            AgentdService agent
    ) {
        this.outbound = new HashMap<>();
        this.queue = new ConcurrentLinkedDeque<>();
    }

    @Override
    public void encode(Object msg, ChannelPromise promise) {
        
        boolean isFirst = false;
        
        if(queue.isEmpty()) {
            isFirst = true;
        }
        
        queue.add(new Runnable(){
            @Override
            public void run() {
                if(msg instanceof ByteBuffer) {
                    inbound.write(msg, promise);
                } else {
                    promise.setFailure(new IllegalArgumentException("Not a ByteBuffer: " + msg));
                }
            }
        });
    }

    @Override
    public void decode(Object msg) {
        if (msg instanceof ByteBuf) {

        } else {

        }
    }

    /**
     * @see Codec#setInbound(com.wincom.dcim.agentd.CodecChannel)
     * @param cc
     */
    @Override
    public void setInbound(CodecChannel cc) {
        this.inbound = cc;
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
        return inbound.withDependencies(r);
    }
}
