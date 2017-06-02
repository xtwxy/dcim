package com.wincom.protocol.modbus.internal.master;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.HandlerContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Composition of TCP connections to a MP3000.
 *
 * @author master
 */
public class MasterCodecImpl implements Codec {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final String ADDRESS_KEY = "ADDRESS";
    public static final String READ_BUFFER_KEY = "READ_BUFFER";
    public static final String MODBUS_REQUEST_KEY = "MODBUS_REQUEST";
    public static final String INBOUND_CONTEXTS_KEY = "INBOUND_CONTEXTS";

    private final MasterDecodeContextImpl decodeContext;
    private final Map<Byte, MasterContextImpl> inboundContexts;

    public MasterCodecImpl() {
        this.inboundContexts = new HashMap<>();
        this.decodeContext = new MasterDecodeContextImpl(inboundContexts);
    }

    @Override
    public HandlerContext openInbound(
            AgentdService service, Properties props, HandlerContext inboundHandler) {
        log.info(String.format("%s", props));

        Byte address = Byte.valueOf(props.getProperty(ADDRESS_KEY));
        // FIXME: add address validation.
        MasterContextImpl inboundContext = inboundContexts.get(address);
        if (inboundContext == null) {
            inboundContext = createInbound0(address, inboundHandler);

            inboundContexts.put(address, inboundContext);
        }

        return inboundContext;
    }

    private MasterContextImpl createInbound0(
            final Byte address,
            final HandlerContext inboundHandler) {

        final MasterContextImpl handlerContext = new MasterContextImpl(address);

        handlerContext.addInboundContext(inboundHandler);

        return handlerContext;
    }

    public MasterDecodeContextImpl getCodecContext() {
        return decodeContext;
    }
}
