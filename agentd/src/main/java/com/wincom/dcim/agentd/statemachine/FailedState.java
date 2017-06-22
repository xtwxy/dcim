package com.wincom.dcim.agentd.statemachine;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.Message;
import static java.lang.System.out;

/**
 *
 * @author master
 */
public class FailedState extends State.Adapter {

    @Override
    public State on(HandlerContext ctx, Message m) {
        out.println("Create server failed.");
        return success();
    }

    @Override
    public boolean stopped() {
        return true;
    }

    @Override
    public String toString() {
        return "FailedState@" + this.hashCode();
    }
}
