package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.protocol.modbus.ModbusFrame;

/**
 *
 * @author master
 */
public class ModbusHandlerContextImpl extends HandlerContext.Adapter {

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
}
