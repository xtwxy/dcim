package com.wincom.dcim.agentd.domain;

/**
 *
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
}
