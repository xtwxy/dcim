package com.wincom.dcim.agentd.domain;

/**
 *
 * @author master
 */
public final class StringSignal extends Signal {
    private String value;

    public StringSignal() {
        super(Type.STRING);
        value = null;
    }

    public String value() {
        return value;
    }

    public void value(String v) {
        value = v;
    }
    
    
}
