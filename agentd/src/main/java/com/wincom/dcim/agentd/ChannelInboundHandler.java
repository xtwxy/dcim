package com.wincom.dcim.agentd;

import com.wincom.dcim.agentd.primitives.Accepted;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.Connected;
import com.wincom.dcim.agentd.primitives.ConnectionClosed;
import com.wincom.dcim.agentd.primitives.ApplicationFailure;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SystemError;
import com.wincom.dcim.agentd.statemachine.StateMachine;

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

    public void handleApplicationFailure(HandlerContext ctx, ApplicationFailure m);

    public void handleSystemError(HandlerContext ctx, SystemError m);

    public void setStateMachine(StateMachine sm);

    public static class Adapter
            extends Handler.Default
            implements ChannelInboundHandler {
        
        protected StateMachine state;
        
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
            ctx.getOutboundHandler().setOutboundContext(m.getContext());
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
        }

        @Override
        public void handlePayloadSent(HandlerContext ctx, Message m) {
            ctx.onRequestCompleted(m);
        }

        @Override
        public void handleApplicationFailure(HandlerContext ctx, ApplicationFailure m) {
            ctx.onRequestCompleted(m);
        }

        @Override
        public void handleSystemError(HandlerContext ctx, SystemError m) {
            ctx.onRequestCompleted(m);
        }

        @Override
        public void setStateMachine(StateMachine sm) {
            state = sm;
        }
    }
}
