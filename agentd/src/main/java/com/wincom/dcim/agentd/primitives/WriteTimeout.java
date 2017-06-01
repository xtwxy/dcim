package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public final class WriteTimeout extends ChannelTimeout {

    public WriteTimeout(HandlerContext c) {
        super(c);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
