package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public interface Message {

    public void apply(HandlerContext ctx, Handler handler);

    public static class Adapter implements Message {

        @Override
        public void apply(HandlerContext ctx, Handler handler) {
            handler.handle(ctx, this);
        }
    }
}
