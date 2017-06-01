package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.HandlerContext;
import java.nio.ByteBuffer;

/**
 *
 * @author master
 */
public class ModbusDecodeContextImpl extends HandlerContext.Adapter {

    private final ByteBuffer readBuffer;

    ModbusDecodeContextImpl() {
        readBuffer = ByteBuffer.allocate(2048);
        set(ModbusCodecImpl.READ_BUFFER_KEY, readBuffer);
    }
}
