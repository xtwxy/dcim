package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public interface Message {

    public void apply(HandlerContext ctx, Handler handler);

    public static class Adapter implements Message {

        private final HandlerContext sender;

        public Adapter(HandlerContext sender) {
            this.sender = sender;
        }

        public HandlerContext getSender() {
            return this.sender;
        }

        @Override
        public void apply(HandlerContext ctx, Handler handler) {
            handler.handle(ctx, this);
        }
    }
}
