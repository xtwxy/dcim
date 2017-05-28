package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public abstract class ChannelInbound implements Message {

    private final HandlerContext context;

    public ChannelInbound(HandlerContext c) {
        this.context = c;
    }

    public HandlerContext getContext() {
        return this.context;
    }

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        if (handler instanceof ChannelInboundHandler) {
            applyChannelInbound(ctx, (ChannelInboundHandler) handler);
        } else {
            handler.handle(ctx, this);
        }
    }

    abstract public void applyChannelInbound(HandlerContext ctx, ChannelInboundHandler handler);
    
    @Override
    public String toString() {
        return String.format("ChannelInbound %s", getContext());
    }
}
