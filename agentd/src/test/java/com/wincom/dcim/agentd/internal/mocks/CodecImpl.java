package com.wincom.dcim.agentd.internal.mocks;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.primitives.Accepted;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelInboundHandler;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.Connected;
import com.wincom.dcim.agentd.primitives.ConnectionClosed;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
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
public class CodecImpl implements Codec, ChannelInboundHandler {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private HandlerContext outboundContext;
    private final Map<Properties, HandlerContext> inbounds;

    CodecImpl() {
        this.inbounds = new HashMap<>();
    }

    @Override
    public HandlerContext openOutbound(
            AgentdService service, Properties props, ChannelInboundHandler inboundHandler) {
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
            ChannelInboundHandler inboundHandler) {
        log.info(props.toString());

        final HandlerContextImpl handlerContext = new HandlerContextImpl();
        handlerContext.setInboundHandler(inboundHandler);

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
    public void handleAccepted(HandlerContext ctx, Accepted m) {
        for (Map.Entry<Properties, HandlerContext> e : inbounds.entrySet()) {
            m.apply(ctx, e.getValue().getInboundHandler());
        }
    }

    @Override
    public void handleConnected(HandlerContext ctx, Connected m) {
        for (Map.Entry<Properties, HandlerContext> e : inbounds.entrySet()) {
            m.apply(ctx, e.getValue().getInboundHandler());
        }
    }

    @Override
    public void handleChannelActive(HandlerContext ctx, ChannelActive m) {
        this.outboundContext = m.getContext();
        for (Map.Entry<Properties, HandlerContext> e : inbounds.entrySet()) {
            OutboundHandlerImpl impl = (OutboundHandlerImpl) e.getValue().getOutboundHandler();
            impl.setDelegate(ctx.getOutboundHandler());
            m.apply(ctx, e.getValue().getInboundHandler());
        }
    }

    @Override
    public void handleChannelInactive(HandlerContext ctx, ChannelInactive m) {
        for (Map.Entry<Properties, HandlerContext> e : inbounds.entrySet()) {
            m.apply(ctx, e.getValue().getInboundHandler());
        }
    }

    @Override
    public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m) {
        for (Map.Entry<Properties, HandlerContext> e : inbounds.entrySet()) {
            m.apply(ctx, e.getValue().getInboundHandler());
        }
    }

    @Override
    public void handleConnectionClosed(HandlerContext ctx, ConnectionClosed m) {
        for (Map.Entry<Properties, HandlerContext> e : inbounds.entrySet()) {
            m.apply(ctx, e.getValue().getInboundHandler());
        }
    }

    @Override
    public void handlePayloadReceived(HandlerContext ctx, Message m) {
        for (Map.Entry<Properties, HandlerContext> e : inbounds.entrySet()) {
            m.apply(ctx, e.getValue().getInboundHandler());
        }
    }

    @Override
    public void handlePayloadSent(HandlerContext ctx, Message m) {
        for (Map.Entry<Properties, HandlerContext> e : inbounds.entrySet()) {
            m.apply(ctx, e.getValue().getInboundHandler());
        }
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        for (Map.Entry<Properties, HandlerContext> e : inbounds.entrySet()) {
            m.apply(ctx, e.getValue().getInboundHandler());
        }
    }
}
