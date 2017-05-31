package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public interface ChannelInboundHandler extends Handler {

    public void handleAccepted(HandlerContext ctx, Accepted m);

    public void handleConnected(HandlerContext ctx, Connected m);

    public void handleChannelActive(HandlerContext ctx, ChannelActive m);

    public void handleChannelInactive(HandlerContext ctx, ChannelInactive m);

    public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m);

    public void handleConnectionClosed(HandlerContext ctx, ConnectionClosed m);

    public void handlePayloadReceived(HandlerContext ctx, Message m);

    public void handlePayloadSent(HandlerContext ctx, Message m);

    public static abstract class Adapter
            extends Handler.Default
            implements ChannelInboundHandler {

        @Override
        public void handleAccepted(HandlerContext ctx, Accepted m) {
            log.info(String.format("handleAccepted(%s, %s)", ctx, m));
        }

        @Override
        public void handleConnected(HandlerContext ctx, Connected m) {
            log.info(String.format("handleConnected(%s, %s)", ctx, m));
        }

        @Override
        public void handleChannelActive(HandlerContext ctx, ChannelActive m) {
            ctx.setActive(true);
        }

        @Override
        public void handleChannelInactive(HandlerContext ctx, ChannelInactive m) {
            ctx.onClosed(m);
        }

        @Override
        public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m) {
            ctx.onRequestCompleted(m);
        }

        @Override
        public void handleConnectionClosed(HandlerContext ctx, ConnectionClosed m) {
            ctx.onRequestCompleted(m);
        }

        @Override
        public void handlePayloadReceived(HandlerContext ctx, Message m) {
            log.info(String.format("handlePayloadReceived(%s, %s)", ctx, m));
        }

        @Override
        public void handlePayloadSent(HandlerContext ctx, Message m) {
            ctx.onRequestCompleted(m);
        }
    }
}
