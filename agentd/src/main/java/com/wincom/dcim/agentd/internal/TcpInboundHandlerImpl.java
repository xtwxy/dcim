package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.TimerInboundHandler;
import com.wincom.dcim.agentd.messages.Accepted;
import com.wincom.dcim.agentd.messages.ChannelActive;
import com.wincom.dcim.agentd.messages.ChannelInactive;
import com.wincom.dcim.agentd.messages.ChannelTimeout;
import com.wincom.dcim.agentd.messages.Connected;
import com.wincom.dcim.agentd.messages.DeadlineTimeout;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.messages.MillsecFromNowTimeout;
import com.wincom.dcim.agentd.messages.PeriodicTimeout;
import com.wincom.dcim.agentd.messages.SystemError;
import com.wincom.dcim.agentd.statemachine.ReceiveState;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import com.wincom.dcim.agentd.statemachine.StateMachineBuilder;

import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 *
 * @author master
 */
public final class TcpInboundHandlerImpl
        extends ChannelInboundHandler.Adapter
        implements TimerInboundHandler {


    TcpInboundHandlerImpl() {
    }

    @Override
    public void handleAccepted(HandlerContext ctx, Accepted m) {
        log.info(String.format("handleAccepted(%s, %s)", ctx, m));

        // fork a new state machine to handle connection.
        final StreamHandlerContextImpl clientContext
                = (StreamHandlerContextImpl) m.getAccepted();

        StateMachine state = new StateMachineBuilder()
                .add("receiveState", new ReceiveState())
                .transision("receiveState", "receiveState", "receiveState", "receiveState")
                .buildWithInitialState("receiveState");

        clientContext.state(state);

        clientContext.getChannel().pipeline()
                .addLast(new LoggingHandler(LogLevel.INFO))
                .addLast(new IdleStateHandler(0, 0, 20))
                .addLast(new StreamChannelInboundHandler(clientContext));

        // continue accepting new connections in this state machine...
        ctx.onRequestCompleted();

        state.enter(clientContext);
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
                .addLast(new IdleStateHandler(0, 0, 5))
                .addLast(new StreamChannelInboundHandler(ctx));

        ctx.onRequestCompleted();

        ctx.state().on(ctx, m);
    }

    @Override
    public void handleChannelActive(HandlerContext ctx, ChannelActive m) {
        log.info(String.format("handleChannelActive(%s, %s)", ctx, m));
        ctx.getOutboundHandler().setOutboundContext(m.getSender());
        ctx.setActive(true);

        ctx.state().on(ctx, m);
        ctx.fireInboundHandlerContexts(new ChannelActive(ctx));
    }

    @Override
    public void handleChannelInactive(HandlerContext ctx, ChannelInactive m) {
        ctx.state().on(ctx, m);
        super.handleChannelInactive(ctx, m);
    }

    @Override
    public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m) {
        ctx.state().on(ctx, m);
        super.handleChannelTimeout(ctx, m);
    }

    @Override
    public void handlePayloadReceived(HandlerContext ctx, Message m) {
        ctx.state().on(ctx, m);
        ctx.fireInboundHandlerContexts(m);
    }

    @Override
    public void handleSystemError(HandlerContext ctx, SystemError m) {
        ctx.state().on(ctx, m);
        super.handleSystemError(ctx, m);
    }

    @Override
    public void handleDeadlineTimeout(HandlerContext ctx, DeadlineTimeout m) {
        ctx.state().on(ctx, m);
        ctx.onRequestCompleted();
    }

    @Override
    public void handleMillsecFromNowTimeout(HandlerContext ctx, MillsecFromNowTimeout m) {
        ctx.state().on(ctx, m);
        ctx.onRequestCompleted();
    }

    @Override
    public void handlePeriodicTimeout(HandlerContext ctx, PeriodicTimeout m) {
        ctx.state().on(ctx, m);
        ctx.onRequestCompleted();
    }
}
