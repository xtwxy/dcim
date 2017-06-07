package com.wincom.dcim.agentd;

import java.util.Properties;

public interface Codec {
    
    /**
     * Create a port 
     * @param service
     * @param props
     * @return 
     */
    public HandlerContext openInbound(AgentdService service, Properties props);
}
