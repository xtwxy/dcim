package com.wincom.dcim.agentd.domain;

import com.wincom.dcim.agentd.domain.Signal;

/**
 *
 * @author master
 */
public class DigitalSignal extends Signal {
    private Boolean value;

    public DigitalSignal() {
        super(Type.DIGITAL);
    }

    public Boolean value() {
        return value;
    }

    public void value(Boolean v) {
        value = v;
    }
    
    
}
