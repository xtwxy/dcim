package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import java.nio.ByteBuffer;
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
public class ModbusCodecImpl implements Codec {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final String ADDRESS_KEY = "address";
    public static final String READ_BUFFER_KEY = "READ_BUFFER";
    public static final String MODBUS_REQUEST_KEY = "MODBUS_REQUEST";

    private final ModbusDecodeContextImpl decodeContext;
    private final Map<Byte, ModbusSlaveContextImpl> inboundContexts;

    public ModbusCodecImpl() {
        this.inboundContexts = new HashMap<>();
        this.decodeContext = new ModbusDecodeContextImpl();
    }

    @Override
    public HandlerContext openInbound(
            AgentdService service, Properties props, HandlerContext inboundHandler) {
        log.info(String.format("%s", props));

        Byte address = Byte.valueOf(props.getProperty(ADDRESS_KEY));
        // FIXME: add address validation.
        ModbusSlaveContextImpl inboundContext = inboundContexts.get(address);
        if (inboundContext == null) {
            inboundContext = createInbound0(address, inboundHandler);

            inboundContexts.put(address, inboundContext);
        }

        return inboundContext;
    }

    private ModbusSlaveContextImpl createInbound0(
            final Byte address,
            final HandlerContext inboundHandler) {

        final ModbusSlaveContextImpl handlerContext = new ModbusSlaveContextImpl(address, decodeContext);

        handlerContext.addInboundContext(inboundHandler);

        return handlerContext;
    }

    public ModbusDecodeContextImpl getCodecContext() {
        return decodeContext;
    }
}
