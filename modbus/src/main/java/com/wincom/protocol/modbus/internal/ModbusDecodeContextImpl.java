package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.primitives.HandlerContext;
import java.nio.ByteBuffer;

/**
 *
 * @author master
 */
public class ModbusDecodeContextImpl extends HandlerContext.Adapter {

    private final ByteBuffer readBuffer;

    ModbusDecodeContextImpl() {
        this.readBuffer = ByteBuffer.allocate(2048);
    }

    public ByteBuffer getReadBuffer() {
        return readBuffer;
    }

}
