package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.ChainedDependency;
import com.wincom.dcim.agentd.CodecChannel;
import com.wincom.dcim.agentd.IoCompletionHandler;
import com.wincom.dcim.agentd.statemachine.StateMachine;

public class DDS3366DCodecChannelImpl
        extends CodecChannel.Adapter {

    private AgentdService agent;

    public DDS3366DCodecChannelImpl(
            int address,
            AgentdService agent
    ) {
        super(agent.getEventLoopGroup());
        this.agent = agent;
    }

    @Override
    public void write(Object msg, IoCompletionHandler handler) {
        ChainedDependency r = new ChainedDependency() {
            @Override
            public void run() {
                DDS3366DCodecChannelImpl.super.write(msg, handler);
            }
        };
        getEventLoopGroup().submit(withDependencies(r));
    }

    @Override
    public StateMachine withDependencies(com.wincom.dcim.agentd.statemachine.StateMachine target) {
        return getInboundCodec().withDependencies(target);
    }
}
