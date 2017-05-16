package com.wincom.dcim.agentd.primitives;

import java.util.Date;

/**
 *
 * @author master
 */
public class SetDeadlineTimer extends Message.Adapter {
    
    private final Date time;
    
    public SetDeadlineTimer(Date time) {
        this.time = time;
    }

    public Date getTime() {
        return time;
    }

    @Override
    public String toString() {
        return String.format("SetDeadlineTimer %s", time);
    }
}
