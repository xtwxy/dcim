package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class SetPeriodicTimer implements Message {

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        handler.handle(ctx, this);
    }
}
