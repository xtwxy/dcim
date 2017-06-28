package com.wincom.dcim.agentd.messages;

import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public interface Message {

    void apply(HandlerContext ctx, Handler handler);

    HandlerContext getSender();

    class Adapter implements Message {

        private final HandlerContext sender;

        protected Adapter(HandlerContext sender) {
            this.sender = sender;
        }

        @Override
        public HandlerContext getSender() {
            return this.sender;
        }

        @Override
        public void apply(HandlerContext ctx, Handler handler) {
            handler.handle(ctx, this);
        }
    }
}
