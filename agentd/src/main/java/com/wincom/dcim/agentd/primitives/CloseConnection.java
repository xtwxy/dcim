package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class CloseConnection implements Message {

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        handler.handle(ctx, this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
