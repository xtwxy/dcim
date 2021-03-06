package com.wincom.dcim.agentd.domain;

import com.wincom.dcim.agentd.util.DateFormat;

import java.util.Date;

/**
 *
 * @author master
 */
public final class TimstampSignal extends Signal {
    private final Date value;
    public TimstampSignal(Date value) {
        super(Type.TIMESTAMP);
        this.value = value;
    }

    public Date value() {
        return value;
    }

    @Override
    public String stringValue() {
        return DateFormat.formatTimestamp(value);
    }

    @Override
    public String toString() {
        return String.format("(%s, \"%s\")", type, DateFormat.formatTimestamp(value));
    }
}
