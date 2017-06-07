package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.protocol.modbus.ReadMultipleHoldingRegistersResponse;
import com.wincom.driver.dds3366d.internal.primitives.ReadSettings.Response;
import java.nio.ByteBuffer;

/**
 *
 * @author master
 */
public class ReadSettingsResponseState extends DefaultReceiveState {

    @Override
    public State on(HandlerContext ctx, Message m) {
        // wait for send request to complete.
        if (m instanceof ReadMultipleHoldingRegistersResponse) {
            // To next state: receive state.
            ReadMultipleHoldingRegistersResponse registers = (ReadMultipleHoldingRegistersResponse) m;
            Response response = new Response(ctx);
            response.fromWire(ByteBuffer.wrap(registers.getBytes()));

            ctx.fireInboundHandlerContexts(response);

            return success();
        } else {
            return super.on(ctx, m);
        }
    }
}
