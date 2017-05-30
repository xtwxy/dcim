package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public final class ConnectFailed extends Message.Adapter {

    @Override
    public String toString() {
        return String.format("%s@%d", getClass().getSimpleName(), hashCode());
    }
}
