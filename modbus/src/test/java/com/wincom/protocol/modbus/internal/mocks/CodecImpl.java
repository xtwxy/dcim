package com.wincom.protocol.modbus.internal.mocks;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.statemachine.ReceiveState;
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
public class CodecImpl extends ChannelInboundHandler.Adapter implements Codec {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private HandlerContext outboundContext;
    private final Map<Properties, HandlerContext> inbounds;

    CodecImpl() {
        this.inbounds = new HashMap<>();
    }

    @Override
    public HandlerContext openInbound(
            AgentdService service, Properties props, HandlerContext inboundHandler) {
        log.info(String.format("%s", props));

        HandlerContext inboundContext = inbounds.get(props);
        if (inboundContext == null) {
            inboundContext = createInbound0(service, props, inboundHandler);

            inbounds.put(props, inboundContext);
        }

        return inboundContext;
    }

    private HandlerContext createInbound0(
            AgentdService service,
            Properties props,
            HandlerContext inboundHandler) {
        log.info(props.toString());

        final HandlerContext handlerContext = new HandlerContextImpl();
        handlerContext.addInboundContext(inboundHandler);
        
        final StateMachineBuilder builder = new StateMachineBuilder();

        StateMachine client = builder
                .add("receiveState", new ReceiveState())
                .transision("receiveState", "receiveState", "receiveState", null)
                .buildWithInitialState("receiveState");

        handlerContext.getInboundHandler().setStateMachine(client);
                client.enter(handlerContext);

        return handlerContext;
    }

    @Override
    public HandlerContext getCodecContext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
