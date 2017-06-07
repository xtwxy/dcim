package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.TimerOutboundHandler;

/**
 *
 * @author master
 */
public final class SetPeriodicTimer extends Message.Adapter {

    public SetPeriodicTimer(HandlerContext sender) {
        super(sender);
    }
    
    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        if (handler instanceof TimerOutboundHandler) {
            ((TimerOutboundHandler) handler).handleSetPeriodicTimer(ctx, this);
        } else {
            handler.handle(ctx, this);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
