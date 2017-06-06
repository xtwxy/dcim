package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.WriteComplete;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.protocol.modbus.ReadMultipleHoldingRegistersRequest;

/**
 *
 * @author master
 */
public class ReadStatusRequestState extends State.Adapter {

    @Override
    public State enter(HandlerContext ctx) {
        ReadMultipleHoldingRegistersRequest request = new ReadMultipleHoldingRegistersRequest();
        request.setStartAddress((short)0x01f4);
        request.setNumberOfRegisters((short)10);

        ctx.send(request);
        
        // FIXME: hack for netty half sync-half-async promise.
        return success();
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
