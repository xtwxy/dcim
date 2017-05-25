package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.primitives.Handler;
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
 * Composition of TCP connections to a MP3000.
 *
 * @author master
 */
public class ModbusCodecImpl implements Codec {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private HandlerContext outboundContext;
    private final Map<Byte, HandlerContext> inboundContexts;

    public ModbusCodecImpl() {
        this.inboundContexts = new HashMap<>();
    }

    @Override
    public void codecActive(HandlerContext outboundContext) {
        this.outboundContext = outboundContext;
        for (Map.Entry<Byte, HandlerContext> e : inboundContexts.entrySet()) {
            e.getValue().initHandlers(outboundContext);
        }
    }

    @Override
    public HandlerContext openInbound(
            AgentdService service,
            Properties props,
            Handler inboundHandler) {
        log.info(String.format("%s", props));

        Byte address = Byte.valueOf(props.getProperty("address"));
        // FIXME: add address validation.
        HandlerContext inboundContext = inboundContexts.get(address);
        if (inboundContext == null) {
            inboundContext = createInbound0(address, inboundHandler);

            inboundContexts.put(address, inboundContext);
        }

        return inboundContext;
    }

    private HandlerContext createInbound0(
            final Byte address,
            final Handler inboundHandler) {

        final HandlerContext handlerContext = new ModbusHandlerContextImpl(address) {
            @Override
            public void close() {
                inboundContexts.remove(address);
            }
        };
        
        handlerContext.setInboundHandler(inboundHandler);
        
        final StateMachineBuilder builder = new StateMachineBuilder();

        StateMachine client = builder
                .add("receiveState", new DefaultReceiveState())
                .transision("receiveState", "receiveState", "receiveState")
                .buildWithInitialState("receiveState");

        handlerContext.getStateMachine()
                .buildWith(client)
                .enter(handlerContext);
        
        handlerContext.initHandlers(this.outboundContext);

        return handlerContext;
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
    }
}
