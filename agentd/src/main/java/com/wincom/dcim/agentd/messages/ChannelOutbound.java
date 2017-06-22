package com.wincom.dcim.agentd.messages;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.ChannelOutboundHandler;

/**
 *
 * @author master
 */
public abstract class ChannelOutbound extends Message.Adapter {

    public ChannelOutbound(HandlerContext sender) {
        super(sender);
    }

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        if (handler instanceof ChannelOutboundHandler) {
            applyChannelOutbound(ctx, (ChannelOutboundHandler) handler);
        } else {
            handler.handle(ctx, this);
        }
    }

    abstract public void applyChannelOutbound(HandlerContext ctx, ChannelOutboundHandler handler);
    
    @Override
    public String toString() {
        return String.format("%s %s", getClass().getSimpleName(), getSender());
    }
}
