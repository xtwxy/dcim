package com.wincom.dcim.agentd.primitives;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public interface ChannelOutboundHandler extends Handler {

    public void handleAccept(HandlerContext ctx, Accept m);

    public void handleConnect(HandlerContext ctx, Connect m);

    public void handleClose(HandlerContext ctx, CloseConnection m);

    public void handleSendPayload(HandlerContext ctx, Message m);

    public static abstract class Adapter 
            extends Handler.Default
            implements ChannelOutboundHandler {

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
    }
}
