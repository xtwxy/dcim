package com.wincom.dcim.agentd.primitives;

import io.netty.channel.Channel;

/**
 *
 * @author master
 */
public class Accepted extends Connected implements Message {

    public Accepted(Channel c) {
        super(c);
    }

    @Override
    public String toString() {
        return String.format("Accepted %s", getChannel());
    }
}
