package com.wincom.dcim.agentd;

import com.wincom.dcim.agentd.messages.Accepted;
import com.wincom.dcim.agentd.messages.ChannelActive;
import com.wincom.dcim.agentd.messages.ChannelInactive;
import com.wincom.dcim.agentd.messages.ChannelTimeout;
import com.wincom.dcim.agentd.messages.Connected;
import com.wincom.dcim.agentd.messages.ConnectionClosed;
import com.wincom.dcim.agentd.messages.ApplicationFailure;
import com.wincom.dcim.agentd.messages.Handler;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.messages.ReadTimeout;
import com.wincom.dcim.agentd.messages.SystemError;
import com.wincom.dcim.agentd.messages.WriteTimeout;

/**
 *
 * @author master
 */
public interface ChannelInboundHandler extends Handler {

    public void handleAccepted(HandlerContext ctx, Accepted m);

    public void handleConnected(HandlerContext ctx, Connected m);

    public void handleChannelActive(HandlerContext ctx, ChannelActive m);

    public void handleChannelInactive(HandlerContext ctx, ChannelInactive m);

    public void handleChannelReadTimeout(HandlerContext ctx, ReadTimeout m);

    public void handleChannelWriteTimeout(HandlerContext ctx, WriteTimeout m);

    public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m);

    public void handleConnectionClosed(HandlerContext ctx, ConnectionClosed m);

    public void handlePayloadReceived(HandlerContext ctx, Message m);

    public void handlePayloadSent(HandlerContext ctx, Message m);

    public void handleApplicationFailure(HandlerContext ctx, ApplicationFailure m);

    public void handleSystemError(HandlerContext ctx, SystemError m);

    public static class Adapter
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
            ctx.getOutboundHandler().setOutboundContext(m.getSender());
            ctx.setActive(true);
            ctx.fireInboundHandlerContexts(new ChannelActive(ctx));
        }

        @Override
        public void handleChannelInactive(HandlerContext ctx, ChannelInactive m) {
            ctx.fireInboundHandlerContexts(new ChannelInactive(ctx));
            ctx.onClosed(m);
        }

        @Override
        public void handleChannelReadTimeout(HandlerContext ctx, ReadTimeout m) {
            ctx.fireInboundHandlerContexts(new ReadTimeout(ctx));
            ctx.onRequestCompleted();
        }

        @Override
        public void handleChannelWriteTimeout(HandlerContext ctx, WriteTimeout m) {
            ctx.fireInboundHandlerContexts(new WriteTimeout(ctx));
            ctx.onRequestCompleted();
        }

        @Override
        public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m) {
            ctx.fireInboundHandlerContexts(new ChannelTimeout(ctx));
            ctx.onRequestCompleted();
        }

        @Override
        public void handleConnectionClosed(HandlerContext ctx, ConnectionClosed m) {
            ctx.onRequestCompleted();
        }

        @Override
        public void handlePayloadReceived(HandlerContext ctx, Message m) {
        }

        @Override
        public void handlePayloadSent(HandlerContext ctx, Message m) {
            ctx.fireInboundHandlerContexts(m);
            ctx.onRequestCompleted();
        }

        @Override
        public void handleApplicationFailure(HandlerContext ctx, ApplicationFailure m) {
            ctx.onRequestCompleted();
            ctx.fireInboundHandlerContexts(m);
        }

        @Override
        public void handleSystemError(HandlerContext ctx, SystemError m) {
            ctx.onRequestCompleted();
        }
    }
}
