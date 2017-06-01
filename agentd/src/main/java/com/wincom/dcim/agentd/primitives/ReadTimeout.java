package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public final class ReadTimeout extends ChannelTimeout {

    public ReadTimeout(HandlerContext c) {
        super(c);
    }
}
