package com.wincom.protocol.modbus;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.AbstractWireable;
import com.wincom.dcim.agentd.messages.Handler;

/**
 *
 * @author master
 */
public abstract class AbstractModbusResponse extends AbstractWireable {

    public AbstractModbusResponse(HandlerContext sender) {
        super(sender);
    }

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        if(handler instanceof ModbusPayloadInboundHandler) {
            applyModbusResponse(ctx, (ModbusPayloadInboundHandler) handler);
        } else {
            handler.handle(ctx, this);
        }
    }
    
    abstract protected void applyModbusResponse(HandlerContext ctx, ModbusPayloadInboundHandler handler);
}
