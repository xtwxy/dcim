package com.wincom.protocol.modbus.internal.mocks;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.HandlerContext.DisposeHandler;
import com.wincom.dcim.agentd.statemachine.ReceiveState;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import com.wincom.dcim.agentd.statemachine.StateMachineBuilder;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class CodecImpl extends ChannelInboundHandler.Adapter implements Codec {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private HandlerContext inboundContext;
    private HandlerContext outboundContext;

    CodecImpl(final HandlerContext outboundHandlerContext) {
        inboundContext = new HandlerContextImpl();
        outboundContext = outboundHandlerContext;
        outboundContext.addInboundContext(inboundContext);
        outboundContext.addDisposeHandler(new DisposeHandler(){
            @Override
            public void onDispose(HandlerContext ctx) {
                outboundHandlerContext.removeInboundContext(ctx);
            }
        });
    }

    @Override
    public HandlerContext openInbound(
            AgentdService service, Properties props) {
        log.info(String.format("%s", props));

        if (inboundContext == null) {
            final StateMachineBuilder builder = new StateMachineBuilder();

            StateMachine client = builder
                    .add("receiveState", new ReceiveState())
                    .transision("receiveState", "receiveState", "receiveState", null)
                    .buildWithInitialState("receiveState");

            inboundContext.state(client);
            client.enter(inboundContext);
        }

        return inboundContext;
    }
}
