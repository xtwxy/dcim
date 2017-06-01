package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import java.nio.ByteBuffer;

/**
 *
 * @author master
 */
public class ModbusDecodeContextImpl extends HandlerContext.Adapter {

    @Override
    public void fire(Message m) {
        for (Handler h : inboundHandlers) {
            m.apply(this, h);
        }
        machine.on(this, m);
    }

}
