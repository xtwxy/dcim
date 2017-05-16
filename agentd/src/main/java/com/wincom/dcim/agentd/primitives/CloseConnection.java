package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class CloseConnection extends Message.Adapter {

    @Override
    public boolean isOob() {
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
