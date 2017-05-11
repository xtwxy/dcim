package com.wincom.dcim.agentd.internal.tests;

import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;
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
