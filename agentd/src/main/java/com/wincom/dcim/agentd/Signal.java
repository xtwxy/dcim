package com.wincom.dcim.agentd;

/**
 *
 * @author master
 */
public abstract class Signal {

    public enum Type {
        ANALOG(0),
        DIGITAL(1),
        STRING(2);

        Type(int code) {
            this.code = code;
        }
        private final int code;
    };

    public Signal(Type type) {
        this.type = type;
    }
    
    abstract public <T extends Object> T value();
    abstract public <T extends Object> void value(T v);
    
    private Type type;
}
