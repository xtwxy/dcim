package com.wincom.dcim.agentd.statemachine;

import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;

/**
 *
 * @author master
 */
public class ReceiveState extends State.Adapter {

    public ReceiveState() {
    }

    @Override
    public State on(HandlerContext ctx, Message m) {        
        if (m instanceof ChannelInactive) {
            ctx.onClosed(m);
            return error();
        } else {
            return success();
        }
    }

    @Override
    public String toString() {
        return "ReceiveState@" + this.hashCode();
    }
}
