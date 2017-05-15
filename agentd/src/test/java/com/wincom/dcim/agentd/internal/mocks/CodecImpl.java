package com.wincom.dcim.agentd.internal.mocks;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import static java.lang.System.out;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author master
 */
public class CodecImpl implements Codec {

    private final HandlerContext inboundContext;
    private final Map<Properties, HandlerContext> outbounds;

    CodecImpl(HandlerContext inboundContext) {
        this.inboundContext = inboundContext;
        this.outbounds = new HashMap<>();
    }

    @Override
    public HandlerContext createOutbound(AgentdService service, Properties inbound) {
        out.println(inbound);

        HandlerContext context = outbounds.get(inbound);
        if (context == null) {
            context = new HandlerContextImpl(inboundContext);
            outbounds.put(inbound, context);
        }
        return context;
    }

}
