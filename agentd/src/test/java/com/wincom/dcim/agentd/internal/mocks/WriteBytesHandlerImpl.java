package com.wincom.dcim.agentd.internal.mocks;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import static java.lang.System.out;

/**
 *
 * @author master
 */
public class WriteBytesHandlerImpl implements Handler {

    private final HandlerContext inbound;

    WriteBytesHandlerImpl(HandlerContext inbound) {
        this.inbound = inbound;
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        out.println(String.format("handle(%s, %s)", ctx, m));
        inbound.send(m, ctx);
    }

}
