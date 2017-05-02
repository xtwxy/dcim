package com.wincom.dcim.agentd.domain;

/**
 *
 * @author master
 */
public class AnalogSignal extends Signal {
    private Double value;

    public AnalogSignal() {
        super(Type.ANALOG);
        value = 0.0d;
    }

    public Double value() {
        return value;
    }

    public void value(Double v) {
        value = v;
    }
    
    
}
