package com.wincom.dcim.agentd;

import com.wincom.dcim.agentd.primitives.ChannelInboundHandler;
import com.wincom.dcim.agentd.primitives.ChannelOutboundHandler;
import java.util.Properties;

public interface Codec {
    
    /**
     * Create a port 
     * @param service
     * @param props
     * @param inboundHandler
     * @return 
     */
    public ChannelOutboundHandler openOutbound(AgentdService service, Properties props, ChannelInboundHandler inboundHandler);
}
