package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.primitives.Accepted;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelInboundHandler;
import com.wincom.dcim.agentd.primitives.ChannelOutboundHandler;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.Connected;
import com.wincom.dcim.agentd.primitives.ConnectionClosed;
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
 * Composition of TCP connections to a MP3000.
 *
 * @author master
 */
public class ModbusCodecImpl implements Codec, ChannelInboundHandler {

    Logger log = LoggerFactory.getLogger(this.getClass());

    public static final String ADDRESS = "address";
    private final ModbusDecodeContextImpl delegate;
    private final Map<Byte, HandlerContext> inboundContexts;

    public ModbusCodecImpl() {
        this.inboundContexts = new HashMap<>();
        this.delegate =new ModbusDecodeContextImpl();
    }

    @Override
    public ChannelOutboundHandler openOutbound(
            AgentdService service, Properties props, ChannelInboundHandler inboundHandler) {
        log.info(String.format("%s", props));

        Byte address = Byte.valueOf(props.getProperty(ADDRESS));
        // FIXME: add address validation.
        HandlerContext inboundContext = inboundContexts.get(address);
        if (inboundContext == null) {
            inboundContext = createInbound0(address, inboundHandler);

            inboundContexts.put(address, inboundContext);
        }

        return inboundContext.getOutboundHandler();
    }

    private HandlerContext createInbound0(
            final Byte address,
            final ChannelInboundHandler inboundHandler) {

        final ModbusHandlerContextImpl handlerContext = new ModbusHandlerContextImpl(address, delegate);
        
        handlerContext.setInboundHandler(inboundHandler);
        
        final StateMachineBuilder builder = new StateMachineBuilder();

        StateMachine client = builder
                .add("receiveState", new DefaultReceiveState())
                .transision("receiveState", "receiveState", "receiveState")
                .buildWithInitialState("receiveState");

        handlerContext.getStateMachine()
                .buildWith(client)
                .enter(handlerContext);
        
        handlerContext.activate(this.delegate);

        return handlerContext;
    }

    @Override
    public void handleChannelActive(HandlerContext ctx, ChannelActive m) {
        this.delegate.activate(m.getContext());
        for (Map.Entry<Byte, HandlerContext> e : inboundContexts.entrySet()) {
            e.getValue().activate(delegate);
        }
    }

    @Override
    public void handleChannelInactive(HandlerContext ctx, ChannelInactive m) {
        this.delegate.activate(m.getContext());
        for (Map.Entry<Byte, HandlerContext> e : inboundContexts.entrySet()) {
            e.getValue().activate(delegate);
        }
    }

    @Override
    public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void handleConnectionClosed(HandlerContext ctx, ConnectionClosed m) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void handlePayloadReceived(HandlerContext ctx, Message m) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void handlePayloadSent(HandlerContext ctx, Message m) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void handleAccepted(HandlerContext ctx, Accepted m) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void handleConnected(HandlerContext ctx, Connected m) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    /**
     * Handle inbound events.
     *
     * @param ctx the outbound handler context.
     * @param m
     */
    @Override
    public void handle(HandlerContext ctx, Message m) {
        log.info(String.format("handle(%s, %s, %s)", this, ctx, m));
        if (m.isOob()) {
            if(m instanceof ChannelActive) {
                codecActive(ctx);
            } else {
                
            }
        }
    }
}
