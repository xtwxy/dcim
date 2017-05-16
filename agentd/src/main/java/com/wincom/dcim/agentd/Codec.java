package com.wincom.dcim.agentd;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import java.util.Properties;

public interface Codec extends Handler {
    public void codecActive(HandlerContext outboundContext);
    public HandlerContext createInbound(AgentdService service, Properties outbound, Handler inboundHandler);
}
