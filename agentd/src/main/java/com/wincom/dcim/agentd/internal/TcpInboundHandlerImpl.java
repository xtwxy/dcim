package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.internal.tests.ReceiveState;
import com.wincom.dcim.agentd.primitives.Accepted;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
                = (StreamHandlerContextImpl) m.getContext();

        state = new StateMachineBuilder()
                .add("receiveState", new ReceiveState())
                .transision("receiveState", "receiveState", "receiveState", "receiveState")
                .buildWithInitialState("receiveState");

        clientContext.getInboundHandler().setStateMachine(state);

        clientContext.getChannel().pipeline()
                .addLast(new com.wincom.dcim.agentd.internal.ChannelInboundHandler(clientContext));

        // continue accepting new connections in this state machine...
        ctx.onRequestCompleted(m);

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
                .addLast(new IdleStateHandler(20, 1, 20))
                .addLast(new com.wincom.dcim.agentd.internal.ChannelInboundHandler(ctx));

        ctx.onRequestCompleted(m);

        state.on(ctx, m);
    }

    @Override
    public void handleChannelActive(HandlerContext ctx, ChannelActive m) {
        super.handleChannelActive(ctx, m);
        ctx.fireInboundHandlerContexts(m);
        state.on(ctx, m);
    }

    @Override
    public void handleChannelInactive(HandlerContext ctx, ChannelInactive m) {
        super.handleChannelInactive(ctx, m);
        ctx.fireInboundHandlerContexts(m);
        state.on(ctx, m);
    }

    @Override
    public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m) {
        super.handleChannelTimeout(ctx, m);
        ctx.fireInboundHandlerContexts(m);
    }

    @Override
    public void handlePayloadReceived(HandlerContext ctx, Message m) {
        ctx.fireInboundHandlerContexts(m);
    }

    @Override
    public void handleSystemError(HandlerContext ctx, SystemError m) {
        super.handleSystemError(ctx, m);
        state.on(ctx, m);
    }

    @Override
    public void handleDeadlineTimeout(HandlerContext ctx, DeadlineTimeout m) {
        ctx.onRequestCompleted(m);
    }

    @Override
    public void handleMillsecFromNowTimeout(HandlerContext ctx, MillsecFromNowTimeout m) {
        state.on(ctx, m);
        ctx.onRequestCompleted(m);
    }

    @Override
    public void handlePeriodicTimeout(HandlerContext ctx, PeriodicTimeout m) {
        ctx.onRequestCompleted(m);
    }
}
