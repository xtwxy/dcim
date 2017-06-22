package com.wincom.protocol.modbus;

import com.wincom.dcim.agentd.messages.AbstractWireable;
import com.wincom.dcim.agentd.messages.Handler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.MessageType;

/**
 *
 * @author master
 */
public abstract class AbstractModbusRequest extends AbstractWireable {

    public AbstractModbusRequest(HandlerContext sender) {
        super(sender);
    }

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
