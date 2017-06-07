package com.wincom.dcim.agentd.internal.mocks;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.ConnectionClosed;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.HandlerContext.DisposeHandler;
import com.wincom.dcim.agentd.primitives.Message;
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
    private final HandlerContext codecContext;

    CodecImpl(HandlerContext outboundHandlerContext) {
        codecContext = new HandlerContextImpl();
        outboundHandlerContext.addInboundContext(codecContext);
        codecContext.addDisposeHandler(new DisposeHandler() {
            @Override
            public void onDispose(HandlerContext ctx) {
                outboundHandlerContext.removeInboundContext(ctx);
            }
        });
        this.inbounds = new HashMap<>();
    }

    @Override
    public HandlerContext openInbound(
            AgentdService service, Properties props) {
        log.info(String.format("%s", props));

        HandlerContext inboundContext = inbounds.get(props);
        if (inboundContext == null) {
            inboundContext = createInbound0(service, props);

            inbounds.put(props, inboundContext);
        }

        return inboundContext;
    }

    private HandlerContext createInbound0(
            AgentdService service,
            Properties props) {
        log.info(props.toString());

        final HandlerContextImpl handlerContext = new HandlerContextImpl();

        return handlerContext;
    }

    @Override
    public void handleChannelActive(HandlerContext ctx, ChannelActive m) {
        this.outboundContext = m.getSender();
        for (Map.Entry<Properties, HandlerContext> e : inbounds.entrySet()) {
            OutboundHandlerImpl outImpl = (OutboundHandlerImpl) e.getValue().getOutboundHandler();
            outImpl.setOutboundContext(ctx);
            e.getValue().fire(m);
        }
    }

    @Override
    public void handleChannelInactive(HandlerContext ctx, ChannelInactive m) {
        for (Map.Entry<Properties, HandlerContext> e : inbounds.entrySet()) {
            e.getValue().fire(m);
        }
    }

    @Override
    public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m) {
        for (Map.Entry<Properties, HandlerContext> e : inbounds.entrySet()) {
            e.getValue().fire(m);
        }
    }

    @Override
    public void handleConnectionClosed(HandlerContext ctx, ConnectionClosed m) {
        for (Map.Entry<Properties, HandlerContext> e : inbounds.entrySet()) {
            e.getValue().fire(m);
        }
    }

    @Override
    public void handlePayloadReceived(HandlerContext ctx, Message m) {
        for (Map.Entry<Properties, HandlerContext> e : inbounds.entrySet()) {
            e.getValue().fire(m);
        }
    }

    @Override
    public void handlePayloadSent(HandlerContext ctx, Message m) {
        for (Map.Entry<Properties, HandlerContext> e : inbounds.entrySet()) {
            e.getValue().fire(m);
        }
    }
}
