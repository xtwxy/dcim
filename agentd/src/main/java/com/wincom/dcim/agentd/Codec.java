package com.wincom.dcim.agentd;

import com.wincom.dcim.agentd.primitives.HandlerContext;
import java.util.Properties;

public interface Codec {
    public HandlerContext createOutbound(AgentdService service, Properties inbound);
}
