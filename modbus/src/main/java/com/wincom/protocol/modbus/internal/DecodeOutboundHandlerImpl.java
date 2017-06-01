package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SendBytes;
import com.wincom.protocol.modbus.ModbusFrame;
import java.nio.ByteBuffer;

/**
 *
 * @author master
 */
public class DecodeOutboundHandlerImpl extends Handler.Default {
    
    private HandlerContext outboundContext;
    
    @Override
    public void handle(HandlerContext ctx, Message m) {
        final ModbusFrame frame = (ModbusFrame) m;
        ByteBuffer buffer = ByteBuffer.allocate(frame.getWireLength());
        frame.toWire(buffer);
        buffer.flip();
        
        outboundContext.send(new SendBytes(ctx, buffer));
    }

    public void setOutboundContext(HandlerContext outboundContext) {
        this.outboundContext = outboundContext;
    }
}
