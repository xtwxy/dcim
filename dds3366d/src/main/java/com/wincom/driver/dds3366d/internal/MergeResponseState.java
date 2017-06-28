package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.GetSignalValues;
import com.wincom.dcim.agentd.messages.Handler;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.statemachine.State;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author master
 */
public class MergeResponseState extends State.Stop {

    @SuppressWarnings("unchecked")
    @Override
    public State enter(HandlerContext ctx) {
        List<Message> responses = (List<Message>) ctx.getOrSetIfNotExist("response", new ArrayList<Message>());
        final GetSignalValues.Response response = new GetSignalValues.Response(ctx);
        final Handler handler = new Handler() {
            @Override
            public void handle(HandlerContext ctx, Message m) {
                GetSignalValues.Response r = (GetSignalValues.Response) m;
                response.getValues().putAll(r.getValues());
            }

        };
        for (Message m : responses) {
            m.apply(ctx, handler);
        }
        ctx.fireInboundHandlerContexts(response);
        ctx.onRequestCompleted();
        return this;
    }

}
