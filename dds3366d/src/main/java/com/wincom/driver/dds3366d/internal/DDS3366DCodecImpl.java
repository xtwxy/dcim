package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.HandlerContext;
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
            e.initHandlers(outboundContext);
        }
    }

    @Override
    public HandlerContext openInbound(AgentdService service, Properties props, Handler inboundHandler) {
        HandlerContext ctx = new DDS3366DHandlerContextImpl(outboundContext) {
            @Override
            public void close() {
                inboundContexts.remove(this);
            }
        };
        ctx.setInboundHandler(inboundHandler);
        inboundContexts.add(ctx);

        return ctx;
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        ModbusFrame request = getRequest(ctx);
    }

    private ModbusFrame getRequest(HandlerContext ctx) {
        return (ModbusFrame) ctx.getCurrentSendingMessage();
    }
}
