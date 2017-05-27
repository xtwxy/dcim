package com.wincom.dcim.agentd.domain;

import java.util.Date;

/**
 *
 * @author master
 */
public class TimstampSignal extends Signal {
    private final Date value;

    public TimstampSignal(Date value) {
        super(Type.TIMESTAMP);
        this.value = value;
    }

    public Date value() {
        return value;
    }
}
