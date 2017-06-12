package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.primitives.Accepted;
import io.netty.channel.Channel;
import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.primitives.Connected;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.TimerInboundHandler;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.DeadlineTimeout;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.MillsecFromNowTimeout;
import com.wincom.dcim.agentd.primitives.PeriodicTimeout;
import com.wincom.dcim.agentd.primitives.SystemError;
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

    private Channel channel;
    private final NetworkService service;

    TcpInboundHandlerImpl(NetworkService service) {
        this.service = service;
    }

    public final void setChannel(Channel channel) {
        this.channel = channel;
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
        ctx.state().on(ctx, m);
        super.handleChannelActive(ctx, m);
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
