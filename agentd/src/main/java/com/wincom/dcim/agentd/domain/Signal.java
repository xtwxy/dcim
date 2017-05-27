package com.wincom.dcim.agentd.domain;

/**
 *
 * @author master
 */
public abstract class Signal {

    public enum Type {
        ANALOG(0),
        DIGITAL(1),
        STRING(2),
        INTEGER(3),
        TIMESTAMP(4);

        Type(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        private final int code;
    };

    public Signal(Type type) {
        this.type = type;
    }

    private Type type;
}
