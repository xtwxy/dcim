package com.wincom.dcim.agentd.internal.tests;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.internal.ChannelInboundHandler;
import com.wincom.dcim.agentd.primitives.Accepted;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.dcim.agentd.statemachine.StateBuilder;
import com.wincom.dcim.agentd.internal.StreamHandlerContextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class AcceptState extends State.Adapter {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private final NetworkService service;

    public AcceptState(NetworkService service) {
        this.service = service;
    }

    @Override
    public State on(HandlerContext context, Message m) {
        if (m instanceof Accepted) {
            Accepted a = (Accepted) m;

            log.info("Connection accepted: " + a.getChannel());

            // fork a new state machine to handle connection.
            // 1.create receive state.
            StateBuilder connection = StateBuilder.initial().state(new ReceiveState());
            // 2.create new context for the newly forked state machine.
            final StreamHandlerContextImpl clientContext
                    = (StreamHandlerContextImpl) service.createHandlerContext();
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
