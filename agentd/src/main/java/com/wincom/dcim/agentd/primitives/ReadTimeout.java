package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class ReadTimeout extends ChannelTimeout {

    public ReadTimeout(HandlerContext c) {
        super(c);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
