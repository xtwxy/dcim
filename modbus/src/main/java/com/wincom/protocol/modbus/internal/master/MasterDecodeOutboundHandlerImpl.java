package com.wincom.protocol.modbus.internal.master;

import com.wincom.dcim.agentd.ChannelOutboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import com.wincom.dcim.agentd.statemachine.StateMachineBuilder;
import com.wincom.protocol.modbus.ModbusFrame;
import com.wincom.protocol.modbus.ModbusPayloadOutboundHandler;
import com.wincom.protocol.modbus.ReadMultipleHoldingRegistersRequest;
import com.wincom.protocol.modbus.WriteMultipleHoldingRegistersRequest;
import com.wincom.protocol.modbus.WriteSingleHoldingRegisterRequest;
import java.util.Map;

/**
 *
 * @author master
 */
public class MasterDecodeOutboundHandlerImpl extends ChannelOutboundHandler.Adapter
        implements ModbusPayloadOutboundHandler {

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
                .add("receive", new MasterReceiveResponseState())
                .add("stop", new State.Stop())
                .transision("send", "receive", "stop", "stop")
                .transision("receive", "stop", "stop", "stop")
                .buildWithInitialAndStop("send", "stop");

        ctx.state(machine);
        machine.enter(ctx);
    }

    @Override
    public void handleReadMultipleHoldingRegistersRequest(HandlerContext ctx, ReadMultipleHoldingRegistersRequest m) {
        handleSendPayload(ctx, m);
    }

    @Override
    public void handleWriteMultipleHoldingRegistersRequest(HandlerContext ctx, WriteMultipleHoldingRegistersRequest m) {
        handleSendPayload(ctx, m);
    }

    @Override
    public void handleWriteSingleHoldingRegisterRequest(HandlerContext ctx, WriteSingleHoldingRegisterRequest m) {
        handleSendPayload(ctx, m);
    }
}
