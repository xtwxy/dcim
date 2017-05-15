package com.wincom.dcim.agentd.internal.mocks;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import static java.lang.System.out;

/**
 *
 * @author master
 */
public class DefaultHandlerImpl implements Handler {

    private final HandlerContext inbound;

    DefaultHandlerImpl(HandlerContext inbound) {
        this.inbound = inbound;
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        out.println(String.format("handle(%s, %s)", ctx, m));
    }

}
