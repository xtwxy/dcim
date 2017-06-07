package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.ChannelOutboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.GetSignalValues;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SetSignalValues;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.driver.dds3366d.internal.primitives.ReadSettings;
import com.wincom.driver.dds3366d.internal.primitives.ReadStatus;

/**
 *
 * @author master
 */
public class PayloadOutboundHandlerImpl
        extends ChannelOutboundHandler.Adapter {

    @Override
    public void handleSendPayload(HandlerContext ctx, Message m) {
        if (m instanceof GetSignalValues.Request) {
            State stop = new State.Stop();
            State state = ReadStatus.initial(m, stop, outboundContext, ctx);
            if (state.stopped()) {
                state = ReadSettings.initial(m, stop, outboundContext, ctx);
            } else {
                state = ReadSettings.initial(m, state, outboundContext, ctx);
            }
            ctx.state(state);
        } else if (m instanceof SetSignalValues.Request) {

        } else {

        }
    }
}
