package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.internal.StreamHandlerContextImpl;
import com.wincom.dcim.agentd.internal.tests.ReceiveState;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import com.wincom.dcim.agentd.statemachine.StateMachineBuilder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

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
            log.info("Connection accepted: " + m);

            // fork a new state machine to handle connection.
            final StreamHandlerContextImpl clientContext
                    = (StreamHandlerContextImpl) m.getContext();

            StateMachine connection = new StateMachineBuilder()
                    .add("receiveState", new ReceiveState())
                    .transision("receiveState", "receiveState", "receiveState", "receiveState")
                    .buildWithInitialState("receiveState");

            clientContext.getChannel().pipeline()
                    .addLast(new com.wincom.dcim.agentd.internal.ChannelInboundHandler(clientContext));

            clientContext.getStateMachine().buildWith(connection).enter(clientContext);

            // continue accepting new connections in this state machine...
            ctx.onRequestCompleted(m);
        }

        @Override
        public void handleConnected(HandlerContext ctx, Connected m) {
            log.info(String.format("Connection established: local: %s, remote:%s",
                    m.getChannel().localAddress(), m.getChannel().remoteAddress()));

            final StreamHandlerContextImpl clientContext
                    = (StreamHandlerContextImpl) ctx;
            clientContext.setChannel(m.getChannel());

            m.getChannel().pipeline()
                    .addLast(new LoggingHandler(LogLevel.INFO))
                    .addLast(new IdleStateHandler(20, 1, 20))
                    .addLast(new com.wincom.dcim.agentd.internal.ChannelInboundHandler(ctx));

            ctx.onRequestCompleted(m);
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
