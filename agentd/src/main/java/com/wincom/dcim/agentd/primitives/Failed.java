package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public final class Failed extends Message.Adapter {

    private Throwable cause;

    public Failed(Throwable cause) {
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }

    @Override
    public String toString() {
        return String.format("%s %s", getClass().getSimpleName(), cause);
    }
}
