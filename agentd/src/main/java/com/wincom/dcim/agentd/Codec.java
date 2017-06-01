package com.wincom.dcim.agentd;

import java.util.Properties;

public interface Codec {
    
    /**
     * Create a port 
     * @param service
     * @param props
     * @param inboundContext
     * @return 
     */
    public HandlerContext openInbound(AgentdService service, Properties props, HandlerContext inboundContext);
    public HandlerContext getCodecContext();
}
