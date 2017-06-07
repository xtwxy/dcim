package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public final class Unknown extends Message.Adapter {

    private final Object message;

    public Unknown(HandlerContext sender, Object msg) {
        super(sender);
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
