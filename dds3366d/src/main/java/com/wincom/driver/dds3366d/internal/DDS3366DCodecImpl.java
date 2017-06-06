package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.HandlerContext;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class DDS3366DCodecImpl implements Codec {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final String ADDRESS_KEY = "ADDRESS";
    public static final String READ_BUFFER_KEY = "READ_BUFFER";
    public static final String MODBUS_REQUEST_KEY = "MODBUS_REQUEST";
    public static final String INBOUND_CONTEXTS_KEY = "INBOUND_CONTEXTS";

    private final DDS3366DHandlerContextImpl decodeContext;

    public DDS3366DCodecImpl() {
        this.decodeContext = new DDS3366DHandlerContextImpl();
    }

    @Override
    public HandlerContext openInbound(AgentdService service, Properties props, HandlerContext inboundContext) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HandlerContext getCodecContext() {
        return decodeContext;
    }
}
