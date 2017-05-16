package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class ChannelTimeout extends Timeout {

    @Override
    public boolean isOob() {
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
