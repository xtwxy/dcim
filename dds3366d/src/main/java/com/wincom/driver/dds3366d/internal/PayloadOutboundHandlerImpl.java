package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.ChannelOutboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.GetSignalValues;
import com.wincom.dcim.agentd.messages.Message;
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
        log.info("handleSendPayload({}, {})", ctx, m);
        if (m instanceof GetSignalValues.Request) {
            State stop = new MergeResponseState();
            State state = ReadSettings.initial(m, stop, outboundContext);
            if (state.stopped()) {
                state = ReadStatus.initial(m, stop, outboundContext);
            } else {
                state = ReadStatus.initial(m, state, outboundContext);
            }
            ctx.state(state);
            if(!state.stopped()) {
                state.enter(ctx);
            }
        } else if (m instanceof SetSignalValues.Request) {
            // TODO: implement DO & AO.
        } else {

        }
    }
}
