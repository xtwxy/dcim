package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.protocol.modbus.ModbusFrame;

/**
 *
 * @author master
 */
public abstract class ModbusHandlerContextImpl extends HandlerContext.Adapter {

    private final byte slaveAddress;
    private final ModbusDecodeContextImpl delegate;

    public ModbusHandlerContextImpl(byte slaveAddress, ModbusDecodeContextImpl delegate) {
        this.slaveAddress = slaveAddress;
        this.delegate = delegate;
    }

    @Override
    public void activate(HandlerContext outboundContext) {
        if (outboundContext != null) {
            setActive(true);
        }
    }

    @Override
    public Handler getHandler(Class clazz) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void send(Message m) {
        if (m instanceof ModbusFrame) {
            ModbusFrame request = (ModbusFrame) m;
            request.setSlaveAddress(slaveAddress);
            ModbusRequestState s = new ModbusRequestState(request, this);
            delegate.enqueueForSendWhenActive(s);
        } else {
            super.send(m);
        }
    }

    @Override
    public void send(Message m, HandlerContext reply) {
        if (m instanceof ModbusFrame) {
            ModbusFrame request = (ModbusFrame) m;
            request.setSlaveAddress(slaveAddress);
            ModbusRequestState s = new ModbusRequestState(request, reply);
            delegate.enqueueForSendWhenActive(s);
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
