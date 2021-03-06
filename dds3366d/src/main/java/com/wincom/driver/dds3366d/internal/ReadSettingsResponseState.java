package com.wincom.driver.dds3366d.internal;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.driver.dds3366d.internal.primitives.ReadSettings.Response;
import com.wincom.protocol.modbus.ReadMultipleHoldingRegistersResponse;

/**
 *
 * @author master
 */
public class ReadSettingsResponseState extends DefaultReceiveState {
    @SuppressWarnings("unchecked")
    @Override
    public State on(HandlerContext ctx, Message m) {
        // wait for send request to complete.
        if (m instanceof ReadMultipleHoldingRegistersResponse) {
            // To next state: receive state.
            ReadMultipleHoldingRegistersResponse registers = (ReadMultipleHoldingRegistersResponse) m;
            Response response = new Response(ctx);
            response.fromWire(ByteBuffer.wrap(registers.getBytes()));

            List<Message> responses = (List<Message>) ctx.getOrSetIfNotExist("response", new ArrayList<>());
            responses.add(response);

            return success();
        } else {
            return super.on(ctx, m);
        }
    }
}
