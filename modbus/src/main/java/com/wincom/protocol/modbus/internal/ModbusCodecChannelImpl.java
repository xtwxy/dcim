package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.CodecChannel;
import com.wincom.dcim.agentd.Connector;
import com.wincom.protocol.modbus.ModbusFrame;
import com.wincom.protocol.modbus.ModbusPayload;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import java.nio.ByteBuffer;

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
    public void write(final Object msg, final ChannelPromise promise) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if(msg instanceof ModbusPayload) {
                    ModbusPayload payload = (ModbusPayload) msg;
                    ModbusFrame frame = new ModbusFrame();
                    frame.setSlaveAddress((byte)(0xff & address));
                    frame.setPayload(payload);
                    ByteBuffer buf = ByteBuffer.allocate(frame.getWireLength());
                    ModbusCodecChannelImpl.super.write(buf, promise);
                } else {
                    promise.setFailure(new IllegalArgumentException("Not a ModbusPayload: " + msg));
                }
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
