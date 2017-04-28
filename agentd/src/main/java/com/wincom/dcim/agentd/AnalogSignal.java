package com.wincom.dcim.agentd;

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

    @Override
    public Double value() {
        return value;
    }

    @Override
    public <T extends Object> void value(T v) {
        value = (Double)v;
    }
    
    
}
