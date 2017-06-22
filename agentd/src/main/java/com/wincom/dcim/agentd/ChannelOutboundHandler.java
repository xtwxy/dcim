package com.wincom.dcim.agentd;

import com.wincom.dcim.agentd.messages.Accept;
import com.wincom.dcim.agentd.messages.CloseConnection;
import com.wincom.dcim.agentd.messages.Connect;
import com.wincom.dcim.agentd.messages.Handler;
import com.wincom.dcim.agentd.messages.Message;

/**
 *
 * @author master
 */
public interface ChannelOutboundHandler extends Handler {

    public void handleAccept(HandlerContext ctx, Accept m);

    public void handleConnect(HandlerContext ctx, Connect m);

    public void handleClose(HandlerContext ctx, CloseConnection m);

    public void handleSendPayload(HandlerContext ctx, Message m);

    public void setOutboundContext(HandlerContext ctx);

    public static abstract class Adapter
            extends Handler.Default
            implements ChannelOutboundHandler {

        protected HandlerContext outboundContext;

        @Override
        public void handleAccept(HandlerContext ctx, Accept m) {
            log.info(String.format("handleAccept(%s, %s)", ctx, m));
        }

        @Override
        public void handleConnect(HandlerContext ctx, Connect m) {
            log.info(String.format("handleConnect(%s, %s)", ctx, m));
        }

        @Override
        public void handleClose(HandlerContext ctx, CloseConnection m) {
            log.info(String.format("handleClose(%s, %s)", ctx, m));
        }

        @Override
        public void setOutboundContext(HandlerContext ctx) {
            outboundContext = ctx;
        }
    }
}
