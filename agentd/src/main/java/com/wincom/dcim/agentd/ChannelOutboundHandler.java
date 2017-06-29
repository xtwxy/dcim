package com.wincom.dcim.agentd;

import com.wincom.dcim.agentd.messages.*;

/**
 * @author master
 */
public interface ChannelOutboundHandler extends Handler {

    void handleAccept(HandlerContext ctx, Accept m);

    void handleConnect(HandlerContext ctx, Connect m);

    void handleAuth(HandlerContext ctx, PasswordAuth m);

    void handleClose(HandlerContext ctx, CloseConnection m);

    void handleSendPayload(HandlerContext ctx, Message m);

    void setOutboundContext(HandlerContext ctx);

    abstract class Adapter
            extends Handler.Default
            implements ChannelOutboundHandler {

        protected HandlerContext outboundContext;

        @Override
        public void handleAccept(HandlerContext ctx, Accept m) {
            log.info("handleAccept({}, {})", ctx, m);
        }

        @Override
        public void handleConnect(HandlerContext ctx, Connect m) {
            log.info("handleConnect({}, {})", ctx, m);
        }

        @Override
        public void handleAuth(HandlerContext ctx, PasswordAuth m) {
            log.info("PasswordAuth({}, {})", ctx, m);
        }
        @Override
        public void handleClose(HandlerContext ctx, CloseConnection m) {
            log.info("handleClose({}, {})", ctx, m);
        }

        @Override
        public void setOutboundContext(HandlerContext ctx) {
            if(ctx == null) {
                log.info("wangxy bundle {}", ctx);
            }
            outboundContext = ctx;
        }
    }
}
