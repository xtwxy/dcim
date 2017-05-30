package com.wincom.dcim.agentd.statemachine;

import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.WriteComplete;

/**
 *
 * @author master
 */
public class ReceiveState extends State.Adapter {

    public ReceiveState() {
    }

    @Override
    public State on(HandlerContext ctx, Message m) {
        m.apply(ctx, ctx.getInboundHandler());
        
        if (m instanceof ChannelInactive) {
            ctx.fireClosed(m);
            return fail();
        } else {
            return success();
        }
    }

    @Override
    public String toString() {
        return "ReceiveState@" + this.hashCode();
    }
}
