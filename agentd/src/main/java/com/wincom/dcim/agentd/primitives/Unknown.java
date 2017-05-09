package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class Unknown implements Message {
    private final Object message;
    public Unknown(Object msg) {
        this.message = msg;
    }
    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        handler.handle(ctx, this);
    }

    public Object getMessage() {
        return message;
    }
}
