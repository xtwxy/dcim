package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class CloseConnection extends Message.Adapter {

    @Override
    public boolean isOob() {
        return true;
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
