package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public final class Unknown extends Message.Adapter {

    private final Object message;

    public Unknown(Object msg) {
        this.message = msg;
    }

    public Object getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("%s %s", getClass().getSimpleName());
    }

}
