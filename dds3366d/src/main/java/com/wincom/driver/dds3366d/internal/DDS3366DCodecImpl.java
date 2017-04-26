package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.CodecChannel;
import com.wincom.dcim.agentd.Dependency;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPromise;

/**
 * Composition of TCP connections to a MP3000.
 *
 * @author master
 */
public class DDS3366DCodecImpl extends Codec.Adapter implements Dependency {

    private CodecChannel inbound;
    private DDS3366DCodecChannelImpl outbound;
    private AgentdService agent;

    /**
     *
     * @param agent reference to agent service.
     */
    public DDS3366DCodecImpl(
            AgentdService agent
    ) {
    }

    @Override
    public void encode(Object msg, ChannelPromise promise) {
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
     * Connect <code>Codec</code> to <code>Codecchannel</code>.
     *
     * @param cc the <code>Codec</code> to be connected.
     */
    @Override
    public void setOutboundCodec(Codec cc) {
        outbound.setOutboundCodec(cc);
    }

    @Override
    public Runnable withDependencies(Runnable r) {
        return inbound.withDependencies(r);
    }
}