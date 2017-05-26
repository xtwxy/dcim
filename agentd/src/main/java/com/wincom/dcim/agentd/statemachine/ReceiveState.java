package com.wincom.dcim.agentd.statemachine;

import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.ReadTimeout;
import com.wincom.dcim.agentd.primitives.WriteComplete;
import com.wincom.dcim.agentd.primitives.WriteTimeout;

/**
 *
 * @author master
 */
public class ReceiveState extends State.Adapter {

    public ReceiveState() {
    }

    @Override
    public State on(HandlerContext ctx, Message m) {
        
        ctx.getInboundHandler().handle(ctx, m);
        
        if (m instanceof BytesReceived) {
            // TODO: notify the inbound handlers.
            return success();
        } else if (m instanceof WriteComplete) {
            // TODO: notify the inbound handlers.
            ctx.onRequestCompleted(m);
            return success();
        } else if (m instanceof WriteTimeout) {
            return success();
        } else if (m instanceof ReadTimeout) {
            ctx.onRequestCompleted(m);
            return success();
        } else if (m instanceof ChannelTimeout) {
            ctx.onRequestCompleted(m);
            return success();
        } else if (m instanceof ChannelActive) {
            ctx.setActive(true);
            // TODO: notify the inbound handlers.
            return success();
        } else if (m instanceof ChannelInactive) {
            ctx.setActive(false);
            ctx.close();
            ctx.fireClosed(m);
            return fail();
        } else {
            log.warn(String.format("unknown state:(%s, %s, %s)", this, ctx, m));
            return success();
        }
    }

    @Override
    public String toString() {
        return "ReceiveState@" + this.hashCode();
    }
}
