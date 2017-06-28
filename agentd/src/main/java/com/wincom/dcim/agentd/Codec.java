package com.wincom.dcim.agentd;

import java.util.Properties;

public interface Codec {
    
    /**
     * Create a port 
     * @param props
     * @return
     */
    HandlerContext openInbound(Properties props);
}
