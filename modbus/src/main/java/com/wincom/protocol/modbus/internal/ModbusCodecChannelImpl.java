package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.CodecChannel;
import com.wincom.dcim.agentd.Connector;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;

public class ModbusCodecChannelImpl
        extends CodecChannel.Adapter
        implements Connector {

    private int address;
    private AgentdService agent;

    public ModbusCodecChannelImpl(
            int address,
            AgentdService agent
    ) {
        super(agent.getEventLoopGroup());
        this.address = address;
        this.agent = agent;
    }

    @Override
    public void onConnect(Channel ch) {
    }

    @Override
    public void write(Object msg, ChannelPromise promise) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                ModbusCodecChannelImpl.super.write(msg, promise);
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
