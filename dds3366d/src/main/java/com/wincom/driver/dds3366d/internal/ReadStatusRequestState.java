package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.messages.WriteComplete;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.protocol.modbus.ReadMultipleHoldingRegistersRequest;

/**
 *
 * @author master
 */
public class ReadStatusRequestState extends State.Adapter {

    private final HandlerContext outbound;

    public ReadStatusRequestState(HandlerContext outbound) {
        this.outbound = outbound;
    }

    @Override
    public State enter(HandlerContext ctx) {
        ReadMultipleHoldingRegistersRequest request = new ReadMultipleHoldingRegistersRequest(ctx);
        request.setStartAddress((short) 0x0000);
        request.setNumberOfRegisters((short) 10);

        outbound.send(request);

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
