package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public final class SetPeriodicTimer extends Message.Adapter {
    
    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        if (handler instanceof TimerHandler) {
            ((TimerHandler) handler).handleSetPeriodicTimer(ctx, this);
        } else {
            handler.handle(ctx, this);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
