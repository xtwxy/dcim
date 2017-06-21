package com.wincom.dcim.agentd.domain;

import com.wincom.dcim.agentd.util.DateFormat;

/**
 * @author master
 */
public abstract class Signal {

    public enum Type {
        ANALOG(0) {
            public Signal create(String value) {
                return new AnalogSignal(Double.valueOf(value));
            }
        },
        DIGITAL(1) {
            public Signal create(String value) {
                return new DigitalSignal(Boolean.valueOf(value));
            }
        },
        STRING(2) {
            public Signal create(String value) {
                return new StringSignal(value);
            }
        },
        INTEGER(3) {
            public Signal create(String value) {
                return new IntegerSignal(Integer.valueOf(value));
            }
        },
        TIMESTAMP(4) {
            public Signal create(String value) {
                return new TimstampSignal(DateFormat.parseTimestamp(value));
            }
        };

        Type(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        abstract public Signal create(String value);

        private final int code;
    }

    public Signal(Type type) {
        this.type = type;
    }

    protected Signal() {

    }
    public Type type() {
        return type;
    }

    public abstract String stringValue();

    protected Type type;
}
