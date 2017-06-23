package com.wincom.dcim.agentd;

import com.wincom.dcim.agentd.messages.*;

/**
 * @author master
 */
public interface ChannelOutboundHandler extends Handler {

    public void handleAccept(HandlerContext ctx, Accept m);

    public void handleConnect(HandlerContext ctx, Connect m);

    public void handleAuth(HandlerContext ctx, PasswordAuth m);

    public void handleClose(HandlerContext ctx, CloseConnection m);

    public void handleSendPayload(HandlerContext ctx, Message m);

    public void setOutboundContext(HandlerContext ctx);

    public static abstract class Adapter
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
            outboundContext = ctx;
        }
    }
}
