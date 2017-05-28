package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class WriteTimeout extends ChannelTimeout {

    public WriteTimeout(HandlerContext c) {
        super(c);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
