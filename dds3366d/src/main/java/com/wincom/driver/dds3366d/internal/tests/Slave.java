package com.wincom.driver.dds3366d.internal.tests;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.internal.NetworkServiceImpl;
import com.wincom.dcim.agentd.internal.StreamChannelInboundHandler;
import com.wincom.dcim.agentd.internal.StreamHandlerContextImpl;
import com.wincom.dcim.agentd.messages.Accept;
import com.wincom.dcim.agentd.messages.AcceptFailed;
import com.wincom.dcim.agentd.messages.Accepted;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import com.wincom.dcim.agentd.statemachine.StateMachineBuilder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 *
 * @author master
 */
public class Slave {

    public static void main(String[] args) {
        NetworkService service = new NetworkServiceImpl();
        HandlerContext handlerContext = service.createStreamHandlerContext();

        final HandlerContext acceptContext = new HandlerContext.Adapter() {
            @Override
            public void fire(Message m) {
                log.info(String.format("%s, %s", this, m));
                if (m instanceof Accepted) {
                    Accepted a = (Accepted) m;
                    final StreamHandlerContextImpl clientContext
                            = (StreamHandlerContextImpl) a.getAccepted();

                    StateMachine state = new StateMachineBuilder()
                            .add("receiveState", new SlaveReceiveRequestState())
                            .transision("receiveState", "receiveState", "receiveState", "receiveState")
                            .buildWithInitialState("receiveState");

                    clientContext.state(state);

                    clientContext.getChannel().pipeline()
                            .addLast(new LoggingHandler(LogLevel.INFO))
                            .addLast(new IdleStateHandler(0, 0, 20))
                            .addLast(new StreamChannelInboundHandler(clientContext));

                    state.enter(clientContext);

                } else if (m instanceof AcceptFailed) {
                    System.exit(1);
                }
            }
        };
        for(int i = 0; i < 8; ++i) {
            handlerContext.send(new Accept(acceptContext, "0.0.0.0", 9080 + i));
        }
    }
}
