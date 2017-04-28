package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.ChainedDependency;
import com.wincom.dcim.agentd.CodecChannel;
import com.wincom.dcim.agentd.Dependency;
import com.wincom.dcim.agentd.IoCompletionHandler;
import com.wincom.protocol.modbus.ModbusFrame;
import com.wincom.protocol.modbus.ModbusPayload;

public class ModbusCodecChannelImpl
        extends CodecChannel.Adapter {

    private final int address;
    private final AgentdService agent;

    public ModbusCodecChannelImpl(
            int address,
            AgentdService agent
    ) {
        super(agent.getEventLoopGroup());
        this.address = address;
        this.agent = agent;
    }

    @Override
    public void write(final Object msg, final IoCompletionHandler handler) {
        ChainedDependency r = new ChainedDependency() {
            @Override
            public void run() {
                if(msg instanceof ModbusPayload) {
                    ModbusPayload payload = (ModbusPayload) msg;
                    ModbusFrame frame = new ModbusFrame();
                    frame.setSlaveAddress((byte)(0xff & address));
                    frame.setPayload(payload);

                    ModbusCodecChannelImpl.super.write(frame, handler);
                } else {
                    fireError(new IllegalArgumentException("Not a ModbusPayload: " + msg));
                }
            }
        };
        getEventLoopGroup().submit(withDependencies(r));
    }

    @Override
    public Dependency withDependencies(Dependency target) {
        return getInboundCodec().withDependencies(target);
    }
}
