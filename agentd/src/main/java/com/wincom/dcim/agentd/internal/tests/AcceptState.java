package com.wincom.dcim.agentd.internal.tests;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.internal.ChannelInboundHandler;
import com.wincom.dcim.agentd.primitives.Accepted;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.dcim.agentd.statemachine.StateBuilder;
import com.wincom.dcim.agentd.internal.StreamHandlerContextImpl;
import com.wincom.dcim.agentd.primitives.Accept;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class AcceptState extends State.Adapter {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private final NetworkService service;
    protected final HandlerContext handlerContext;
    protected final String host;
    protected final int port;

    public AcceptState(NetworkService service,
            HandlerContext handlerContext,
            String host,
            int port
    ) {
        this.service = service;
        this.handlerContext = handlerContext;
        this.host = host;
        this.port = port;

    }

    @Override
    public State enter(HandlerContext ctx) {
        this.handlerContext.send(new Accept(host, port));
        return this;
    }

    @Override
    public State on(HandlerContext context, Message m) {
        if (m instanceof Accepted) {
            Accepted a = (Accepted) m;

            log.info("Connection accepted: " + a.getChannel());

            // fork a new state machine to handle connection.
            // 1.create new context for the newly forked state machine.
            final StreamHandlerContextImpl clientContext
                    = (StreamHandlerContextImpl) service.createHandlerContext();
            // 2.create receive state.
            StateBuilder connection = StateBuilder.initial().state(new ReceiveState(clientContext));
            // 3.bind context to the newly forked state machine.
            clientContext.getStateMachine().buildWith(connection);
            // 4.set channel for communication with underlying service...
            clientContext.setChannel(a.getChannel());
            // 5.initialize event generator for this channel.
            a.getChannel().pipeline()
                    //.addLast(new IdleStateHandler(0, 0, 6))
                    .addLast(new ChannelInboundHandler(clientContext));

            // continue accepting new connections in this state machine...
            context.onSendComplete(m);
            return this;
        } else {
            return fail();
        }
    }

    @Override
    public String toString() {
        return "AcceptState@" + this.hashCode();
    }
}
