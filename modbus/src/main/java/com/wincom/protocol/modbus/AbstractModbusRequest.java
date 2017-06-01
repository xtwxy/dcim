package com.wincom.protocol.modbus;

import com.wincom.dcim.agentd.primitives.AbstractWireable;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public abstract class AbstractModbusRequest extends AbstractWireable {

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        if(handler instanceof ModbusPayloadOutboundHandler) {
            applyModbusRequest(ctx, (ModbusPayloadOutboundHandler) handler);
        } else {
            handler.handle(ctx, this);
        }
    }
    
    abstract public void applyModbusRequest(HandlerContext ctx, ModbusPayloadOutboundHandler handler);
}
