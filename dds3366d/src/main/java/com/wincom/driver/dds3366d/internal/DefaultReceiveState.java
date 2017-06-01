package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;

/**
 *
 * @author master
 */
public class DefaultReceiveState extends State.Adapter {

    @Override
    public State on(HandlerContext ctx, Message m) {
        
        if (m instanceof ChannelTimeout) {
            ctx.onRequestCompleted(m);
            return success();
        } else if (m instanceof ChannelActive) {
            ctx.setActive(true);
            ctx.getInboundHandler().handle(ctx, m);
            // TODO: notify the inbound handlers.
            return success();
        } else if (m instanceof ChannelInactive) {
            ctx.setActive(false);
            ctx.close();
            ctx.onClosed(m);
            return error();
        } else {
            log.info(String.format("default: (%s, %s, %s), leave to the inbound handler.", this, ctx, m));
            ctx.getInboundHandler().handle(ctx, m);
            return success();
        }
    }

    @Override
    public String toString() {
        return "DefaultReceiveState@" + this.hashCode();
    }
}
