package com.wincom.protocol.modbus.internal.master;

import com.wincom.dcim.agentd.ChannelOutboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import com.wincom.dcim.agentd.statemachine.StateMachineBuilder;
import com.wincom.protocol.modbus.ModbusFrame;
import java.util.Map;

/**
 *
 * @author master
 */
public class MasterDecodeOutboundHandlerImpl extends ChannelOutboundHandler.Adapter {
    private final Map<Byte, MasterContextImpl> inboundContexts;

    public MasterDecodeOutboundHandlerImpl(Map<Byte, MasterContextImpl> inboundContexts) {
        this.inboundContexts = inboundContexts;
    }

    @Override
    public void handleSendPayload(HandlerContext ctx, Message m) {
        ModbusFrame request = (ModbusFrame) m;
        StateMachineBuilder builder = new StateMachineBuilder();
        StateMachine machine = builder
                .add("send", new MasterSendRequestState(request, outboundContext))
                .add("receive", new MasterReceiveResponseState(inboundContexts.get(request.getSlaveAddress())))
                .add("stop", new State.Adapter())
                .transision("send", "receive", "stop", "stop")
                .transision("receive", "stop", "stop", "stop")
                .transision("stop", "stop", "stop", "stop")
                .buildWithInitialAndStop("send", "stop");
        
        ctx.getInboundHandler().setStateMachine(machine);
        machine.enter(ctx);
    }
}
