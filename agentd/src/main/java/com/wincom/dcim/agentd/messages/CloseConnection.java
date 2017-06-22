package com.wincom.dcim.agentd.messages;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.ChannelOutboundHandler;

/**
 *
 * @author master
 */
public final class CloseConnection extends Message.Adapter {

    public CloseConnection(HandlerContext sender) {
        super(sender);
    }

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        if (handler instanceof ChannelOutboundHandler) {
            ((ChannelOutboundHandler) handler).handleClose(ctx, this);
        } else {
            handler.handle(ctx, this);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
