package com.wincom.protocol.modbus.internal.master;

import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.HandlerContext.DisposeHandler;
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
    
    public static final String MODBUS_SLAVE_ADDRESS_KEY = "ADDRESS";
    public static final String READ_BUFFER_KEY = "READ_BUFFER";
    public static final String MODBUS_REQUEST_KEY = "MODBUS_REQUEST";
    public static final String INBOUND_CONTEXTS_KEY = "INBOUND_CONTEXTS";
    
    private final MasterDecodeContextImpl decodeContext;
    private final Map<Byte, MasterContextImpl> inboundContexts;
    
    public MasterCodecImpl(HandlerContext outboundHandlerContext) {
        inboundContexts = new HashMap<>();
        decodeContext = new MasterDecodeContextImpl(inboundContexts);
        outboundHandlerContext.addInboundContext(decodeContext);
        decodeContext.addDisposeHandler(new DisposeHandler() {
            @Override
            public void onDispose(HandlerContext ctx) {
                outboundHandlerContext.removeInboundContext(ctx);
            }
        });
    }
    
    @Override
    public HandlerContext openInbound(
            Properties props) {
        log.debug(String.format("%s", props));
        
        Byte address = Byte.valueOf(props.getProperty(MODBUS_SLAVE_ADDRESS_KEY));
        // FIXME: add address validation.
        MasterContextImpl inboundContext = inboundContexts.get(address);
        if (inboundContext == null) {
            inboundContext = new MasterContextImpl(address);
            
            inboundContext.addDisposeHandler(new DisposeHandler() {
                @Override
                public void onDispose(HandlerContext ctx) {
                    inboundContexts.remove(address);
                }
            });
            
            inboundContexts.put(address, inboundContext);
        }
        
        return inboundContext;
    }
    
    public MasterDecodeContextImpl getCodecContext() {
        return decodeContext;
    }
}
