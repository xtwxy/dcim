package com.wincom.dcim.agentd.domain;

/**
 * @author master
 */
public final class StringSignal extends Signal {
    private String value;

    public StringSignal(String s) {
        super(Type.STRING);
        value = s;
    }

    public String value() {
        return value;
    }

    public void value(String v) {
        value = v;
    }

    @Override
    public String stringValue() {
        return String.format("%s", value);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", type, value);
    }
}
