package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelInboundHandler;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Composition of TCP connections to a MP3000.
 *
 * @author master
 */
public class ModbusCodecImpl
        extends ChannelInboundHandler.Adapter
        implements Codec {

    public static final String ADDRESS_KEY = "address";
    public static final String READ_BUFFER_KEY = "READ_BUFFER";
    public static final String MODBUS_REQUEST_KEY = "MODBUS_REQUEST";

    private final ByteBuffer readBuffer;
    private final ModbusDecodeContextImpl decodeContext;
    private final Map<Byte, ModbusSlaveContextImpl> inboundContexts;

    public ModbusCodecImpl() {
        this.inboundContexts = new HashMap<>();
        this.decodeContext = new ModbusDecodeContextImpl();
        readBuffer = (ByteBuffer) this.decodeContext.getOrSetIfNotExist(READ_BUFFER_KEY, ByteBuffer.allocate(2048));
    }

    @Override
    public HandlerContext openOutbound(
            AgentdService service, Properties props, Handler inboundHandler) {
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
            final Handler inboundHandler) {

        final ModbusSlaveContextImpl handlerContext = new ModbusSlaveContextImpl(address, decodeContext);

        handlerContext.addInboundHandler(inboundHandler);

        return handlerContext;
    }

    @Override
    public void handleChannelActive(HandlerContext ctx, ChannelActive m) {
        this.decodeContext.setActive(true);
        for (Map.Entry<Byte, ModbusSlaveContextImpl> e : inboundContexts.entrySet()) {
            e.getValue().fire(m);
        }
    }

    @Override
    public void handleChannelInactive(HandlerContext ctx, ChannelInactive m) {
        this.decodeContext.setActive(false);
        for (Map.Entry<Byte, ModbusSlaveContextImpl> e : inboundContexts.entrySet()) {
            e.getValue().fire(m);
        }
    }

    @Override
    public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m) {
        ctx.onRequestCompleted(m);
        for (Map.Entry<Byte, ModbusSlaveContextImpl> e : inboundContexts.entrySet()) {
            e.getValue().fire(m);
        }
    }

    @Override
    public void handlePayloadReceived(HandlerContext ctx, Message m) {
        BytesReceived b = (BytesReceived) m;
        readBuffer.put(b.getByteBuffer());
    }

    @Override
    public void handlePayloadSent(HandlerContext ctx, Message m) {
    }
}
