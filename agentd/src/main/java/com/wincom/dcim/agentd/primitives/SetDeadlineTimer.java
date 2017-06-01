package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.HandlerContext;
import java.util.Date;
import com.wincom.dcim.agentd.TimerOutboundHandler;

/**
 *
 * @author master
 */
public final class SetDeadlineTimer extends Message.Adapter {
    
    private final Date time;
    
    public SetDeadlineTimer(Date time) {
        this.time = time;
    }

    public Date getTime() {
        return time;
    }
    
    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        if (handler instanceof TimerOutboundHandler) {
            ((TimerOutboundHandler) handler).handleSetDeadlineTimer(ctx, this);
        } else {
            handler.handle(ctx, this);
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s", getClass().getSimpleName(), time);
    }
}
