package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.internal.ChannelInboundHandler;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import com.wincom.dcim.agentd.statemachine.StateMachineBuilder;
import com.wincom.protocol.modbus.ModbusFrame;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class DDS3366DCodecImpl implements Codec {

    Logger log = LoggerFactory.getLogger(this.getClass());
    private HandlerContext outboundContext;
    private final Set<HandlerContext> inboundContexts;

    public DDS3366DCodecImpl() {
        inboundContexts = new HashSet<>();
    }

    @Override
    public void codecActive(HandlerContext outboundContext) {
        this.outboundContext = outboundContext;
        for (HandlerContext e : inboundContexts) {
            e.activate(outboundContext);
        }
    }

    @Override
    public ChannelInboundHandler openOutbound(AgentdService service, Properties props, ChannelOutboundHandler inboundHandler) {
        HandlerContext ctx = new DDS3366DHandlerContextImpl(outboundContext) {
            @Override
            public void close() {
                inboundContexts.remove(this);
            }
        };
        inboundContexts.add(ctx);

        ctx.setInboundHandler(inboundHandler);
        ctx.activate(this.outboundContext);
        
        final StateMachineBuilder builder = new StateMachineBuilder();

        StateMachine client = builder
                .add("receiveState", new DefaultReceiveState())
                .transision("receiveState", "receiveState", "receiveState", null)
                .buildWithInitialState("receiveState");

        ctx.getStateMachine()
                .buildWith(client)
                .enter(ctx);
        
        return ctx;
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        log.info(String.format("handle(%s, %s, %s)", this, ctx, m));
    }
}
