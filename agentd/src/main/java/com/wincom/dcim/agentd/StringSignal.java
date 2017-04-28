package com.wincom.dcim.agentd;

/**
 *
 * @author master
 */
public class StringSignal extends Signal {
    private String value;

    public StringSignal() {
        super(Type.STRING);
        value = null;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public <T extends Object> void value(T v) {
        value = (String)v;
    }
    
    
}
