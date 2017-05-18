package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class ConnectFailed extends Message.Adapter {

    @Override
    public String toString() {
        return String.format("ConnectFailed@%d", hashCode());
    }
}
