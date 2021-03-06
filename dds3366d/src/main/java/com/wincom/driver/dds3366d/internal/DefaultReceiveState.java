package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.messages.ChannelActive;
import com.wincom.dcim.agentd.messages.ChannelInactive;
import com.wincom.dcim.agentd.messages.ChannelTimeout;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.statemachine.State;

/**
 *
 * @author master
 */
public class DefaultReceiveState extends State.Adapter {

    @Override
    public State on(HandlerContext ctx, Message m) {
        
        if (m instanceof ChannelTimeout) {
            ctx.onRequestCompleted();
            return success();
        } else if (m instanceof ChannelActive) {
            ctx.setActive(true);
            // TODO: notify the inbound handlers.
            return success();
        } else if (m instanceof ChannelInactive) {
            ctx.setActive(false);
            ctx.onClosed(m);
            return error();
        } else {
            log.info(String.format("default: (%s, %s, %s), leave to the inbound handler.", this, ctx, m));
            return success();
        }
    }

    @Override
    public String toString() {
        return String.format("%s@%s", getClass().getSimpleName(), this.hashCode());
    }
}
