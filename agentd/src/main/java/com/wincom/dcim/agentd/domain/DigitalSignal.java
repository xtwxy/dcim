package com.wincom.dcim.agentd.domain;

/**
 * @author master
 */
public final class DigitalSignal extends Signal {
    private final Boolean value;

    public DigitalSignal(Boolean value) {
        super(Type.DIGITAL);
        this.value = value;
    }

    public Boolean value() {
        return value;
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
