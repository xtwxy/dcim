package com.wincom.dcim.agentd.internal.mocks;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.internal.tests.ReceiveState;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import com.wincom.dcim.agentd.statemachine.StateMachineBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class CodecImpl implements Codec {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private HandlerContext outboundContext;
    private final Map<Properties, HandlerContext> inbounds;

    CodecImpl() {
        this.inbounds = new HashMap<>();
    }

    @Override
    public HandlerContext createInbound(
            AgentdService service,
            Properties outboundProps,
            Handler inboundHandler) {
        log.info(String.format("%s", outboundProps));

        HandlerContext inboundContext = inbounds.get(outboundProps);
        if (inboundContext == null) {
            inboundContext = createInbound0(service, outboundProps, inboundHandler);

            inbounds.put(outboundProps, inboundContext);
        }

        return inboundContext;
    }

    private HandlerContext createInbound0(
            AgentdService service,
            Properties outboundProps,
            Handler inboundHandler) {
        log.info(outboundProps.toString());

        final HandlerContext handlerContext = new HandlerContextImpl();
        final StateMachineBuilder builder = new StateMachineBuilder();

        StateMachine client = builder
                .add("receiveState", new ReceiveState())
                .transision("receiveState", "receiveState", "receiveState")
                .buildWithInitialState("receiveState");

        handlerContext.getStateMachine()
                .buildWith(client)
                .enter(handlerContext);

        return handlerContext;
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        if (m.isOob()) {
            if(m instanceof ChannelActive) {
                codecActive(ctx);
            }
        }
        for (Map.Entry<Properties, HandlerContext> e : inbounds.entrySet()) {
            e.getValue().fire(m);
        }
    }

    @Override
    public void codecActive(HandlerContext outboundContext) {
        this.outboundContext = outboundContext;
        for (Map.Entry<Properties, HandlerContext> e : inbounds.entrySet()) {
            e.getValue().initHandlers(outboundContext);
        }
    }
}
