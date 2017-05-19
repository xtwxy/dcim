package com.wincom.dcim.agentd;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import java.util.Properties;

public interface Codec extends Handler {
    /**
     * Callback when <code>Codec</code> is activated.
     * @param outboundContext 
     */
    public void codecActive(HandlerContext outboundContext);
    
    /**
     * Create a port 
     * @param service
     * @param props
     * @param inboundHandler
     * @return 
     */
    public HandlerContext openInbound(AgentdService service, Properties props, Handler inboundHandler);
}
