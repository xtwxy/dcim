package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.CodecChannel;
import com.wincom.dcim.agentd.Connector;
import com.wincom.dcim.agentd.IoCompletionHandler;
import io.netty.channel.Channel;

public class DDS3366DCodecChannelImpl
        extends CodecChannel.Adapter
        implements Connector {

    private AgentdService agent;

    public DDS3366DCodecChannelImpl(
            int address,
            AgentdService agent
    ) {
        super(agent.getEventLoopGroup());
        this.agent = agent;
    }

    @Override
    public void onConnect(Channel ch) {
    }

    @Override
    public void write(Object msg, IoCompletionHandler handler) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                DDS3366DCodecChannelImpl.super.write(msg, handler);
            }
        };
        getEventLoopGroup().submit(withDependencies(r));
    }

    @Override
    public Runnable withDependencies(Runnable target) {
        Runnable r = target;
        // TODO: implement dependencies.
        return r;
    }
}
