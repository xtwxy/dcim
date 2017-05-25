package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.WriteComplete;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.driver.dds3366d.internal.primitives.ReadSettings;

/**
 *
 * @author master
 */
public class SendRequestState extends State.Adapter {

    @Override
    public State enter(HandlerContext ctx) {
        // install request completion handler.
        // send request
        ReadSettings.Request r = new ReadSettings.Request();
        ctx.getHandler(ReadSettings.Request.class).handle(ctx, r);
        
        return this;
    }

    @Override
    public State on(HandlerContext ctx, Message m) {
        // wait for send request to complete.
        if (m instanceof WriteComplete) {
            // To next state: receive state.
            return success();
        } else if (m instanceof ChannelTimeout) {
            ctx.onSendComplete(m);
            return success();
        } else if (m instanceof ChannelActive) {
            ctx.setActive(true);
            ctx.getInboundHandler().handle(ctx, m);
            // TODO: notify the inbound handlers.
            return this;
        } else if (m instanceof ChannelInactive) {
            ctx.setActive(false);
            ctx.close();
            ctx.fireClosed(m);
            return fail();
        } else {
            log.info(String.format("default: (%s, %s, %s), leave to the inbound handler.", this, ctx, m));
            ctx.getInboundHandler().handle(ctx, m);
            return success();
        }
    }

    @Override
    public State exit(HandlerContext ctx) {
        // un-install request completion handler.
        return this;
    }
}
