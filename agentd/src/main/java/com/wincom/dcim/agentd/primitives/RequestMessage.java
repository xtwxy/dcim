package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.ChannelOutbound;

/**
 * @author master
 */
public abstract class RequestMessage extends ChannelOutbound {
    private final PrimitiveMessageType type;

    public RequestMessage(HandlerContext sender, PrimitiveMessageType type) {
        super(sender);
        this.type = type;
    }

    public PrimitiveMessageType type() {
        return type;
    }
}
