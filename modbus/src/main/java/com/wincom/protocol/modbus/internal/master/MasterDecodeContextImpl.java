package com.wincom.protocol.modbus.internal.master;

import com.wincom.dcim.agentd.HandlerContext;
import java.nio.ByteBuffer;

/**
 *
 * @author master
 */
public class MasterDecodeContextImpl extends HandlerContext.Adapter {

    private final ByteBuffer readBuffer;

    MasterDecodeContextImpl() {
        readBuffer = ByteBuffer.allocate(2048);
        set(MasterCodecImpl.READ_BUFFER_KEY, readBuffer);
        inboundHandler = new MasterDecodeInboundHandlerImpl();
        outboundHandler = new MasterDecodeOutboundHandlerImpl();
    }
}
