package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public interface Message {
    public void apply(Handler handler);
}
