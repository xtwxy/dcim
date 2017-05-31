 package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.internal.tests.ReceiveState;
import com.wincom.dcim.agentd.primitives.Accepted;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wincom.dcim.agentd.primitives.ChannelInboundHandler;
import com.wincom.dcim.agentd.primitives.Connected;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import com.wincom.dcim.agentd.statemachine.StateMachineBuilder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 *
 * @author master
 */
public final class TcpInboundHandlerImpl extends ChannelInboundHandler.Adapter {

    private Logger log = LoggerFactory.getLogger(this.getClass());

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
    public String toString() {
        return "TcpInboundHandlerImpl@" + this.hashCode();
    }
}
