package com.wincom.dcim.agentd.domain;

/**
 * @author master
 */
public final class AnalogSignal extends Signal {

    private final Double value;

    public AnalogSignal(Double value) {
        super(Type.ANALOG);
        this.value = value;
    }

    public Double value() {
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
