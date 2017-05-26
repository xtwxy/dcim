package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.WriteComplete;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.driver.dds3366d.internal.primitives.ReadSettings.Request;

/**
 *
 * @author master
 */
public class ReadSettingsRequestState extends State.Adapter {

    @Override
    public State enter(HandlerContext ctx) {

        ctx.getHandler(Request.class).handle(ctx, new Request());

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
