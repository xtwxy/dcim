package com.wincom.dcim.agentd;

/**
 *
 * @author master
 */
public class DigitalSignal extends Signal {
    private Boolean value;

    public DigitalSignal() {
        super(Type.DIGITAL);
    }

    @Override
    public Boolean value() {
        return value;
    }

    @Override
    public <T extends Object> void value(T v) {
        value = (Boolean)v;
    }
    
    
}
