package com.wincom.dcim.agentd.primitives;

import java.util.Date;

/**
 *
 * @author master
 */
public class SetDeadlineTimer implements Message {
    
    private final Date time;
    
    public SetDeadlineTimer(Date time) {
        this.time = time;
    }

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        handler.handle(ctx, this);
    }

    public Date getTime() {
        return time;
    }

    @Override
    public String toString() {
        return String.format("SetDeadlineTimer %s", time);
    }
}
