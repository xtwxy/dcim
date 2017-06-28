package com.wincom.dcim.agentd.messages;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.TimerInboundHandler;

/**
 *
 * @author master
 */
public abstract class Timeout extends Message.Adapter {

    Timeout(HandlerContext sender) {
        super(sender);
    }

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        if (handler instanceof TimerInboundHandler) {
            applyTimout(ctx, (TimerInboundHandler) handler);
        } else {
            handler.handle(ctx, this);
        }
    }

    abstract protected void applyTimout(HandlerContext ctx, TimerInboundHandler handler);

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
