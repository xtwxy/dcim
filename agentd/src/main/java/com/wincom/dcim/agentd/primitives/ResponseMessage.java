package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.ChannelInbound;

/**
 * @author master
 */
public abstract class ResponseMessage extends ChannelInbound {
    private final PrimitiveMessageType type;

    public ResponseMessage(HandlerContext sender, PrimitiveMessageType type) {
        super(sender);
        this.type = type;
    }
}
