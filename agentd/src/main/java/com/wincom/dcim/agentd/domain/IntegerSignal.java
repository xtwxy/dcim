package com.wincom.dcim.agentd.domain;

/**
 *
 * @author master
 */
public class IntegerSignal extends Signal {
    private final Integer value;

    public IntegerSignal(Integer value) {
        super(Type.INTEGER);
        this.value = value;
    }

    public Integer value() {
        return value;
    }
}
