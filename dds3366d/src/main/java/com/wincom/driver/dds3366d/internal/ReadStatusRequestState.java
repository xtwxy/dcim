package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.WriteComplete;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.protocol.modbus.ModbusFrame;
import com.wincom.protocol.modbus.ReadMultipleHoldingRegistersRequest;

/**
 *
 * @author master
 */
public class ReadStatusRequestState extends State.Adapter {

    @Override
    public State enter(HandlerContext ctx) {
        DDS3366DHandlerContextImpl ctxImpl = (DDS3366DHandlerContextImpl) ctx;
        ModbusFrame frame = new ModbusFrame();
        ReadMultipleHoldingRegistersRequest request = new ReadMultipleHoldingRegistersRequest();
        request.setStartAddress((short)0x01f4);
        request.setNumberOfRegisters((short)10);
        frame.setPayload(request);

        ctxImpl.getOutboundContext().send(frame, ctx);
        
        return this;
    }

    @Override
    public State on(HandlerContext ctx, Message m) {
        // wait for send request to complete.
        if (m instanceof WriteComplete) {
            // To next state: receive state.
            return success();
        } else {
            return super.on(ctx, m);
        }
    }
}
