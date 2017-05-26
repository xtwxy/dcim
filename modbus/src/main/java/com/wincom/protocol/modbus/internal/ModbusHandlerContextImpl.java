package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SendBytes;
import com.wincom.dcim.agentd.primitives.Unknown;
import com.wincom.protocol.modbus.ModbusFrame;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author master
 */
public abstract class ModbusHandlerContextImpl extends HandlerContext.Adapter {

    private byte slaveAddress;
    private final Map<Class, Handler> handlers;

    public ModbusHandlerContextImpl(byte slaveAddress) {
        this.slaveAddress = slaveAddress;
        this.handlers = new HashMap<>();
    }

    @Override
    public Handler getHandler(Class clazz) {
        Handler h = handlers.get(clazz);
        if (h == null) {
            h = handlers.get(Unknown.class);
        }
        return h;
    }

    @Override
    public void initHandlers(HandlerContext outboundContext) {
        this.handlers.put(SendBytes.class, new SendRequestHandlerImpl(outboundContext));
        this.handlers.put(Unknown.class, new DefaultHandlerImpl(outboundContext));
    }

    @Override
    public void send(Message m) {
        if (m instanceof ModbusFrame) {
            ModbusFrame request = (ModbusFrame) m;
            request.setSlaveAddress(slaveAddress);
            ModbusRequestState s = new ModbusRequestState(request, this);
            enqueueForSendWhenActive(s);
        } else {
            super.send(m);
        }
    }

    @Override
    public void send(Message m, HandlerContext reply) {
        if (m instanceof ModbusFrame) {
            ModbusRequestState s = new ModbusRequestState((ModbusFrame) m, reply);
            enqueueForSendWhenActive(s);
        } else {
            super.send(m, reply);
        }
    }

    @Override
    public void fire(Message m) {
        if (isInprogress()) {
            current.on(this, m);
        } else {
            machine.on(this, m);
        }
    }
}
