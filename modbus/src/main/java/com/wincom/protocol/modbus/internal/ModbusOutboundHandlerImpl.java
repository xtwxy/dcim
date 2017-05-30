package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.primitives.ChannelInboundHandler;
import com.wincom.dcim.agentd.primitives.ChannelOutboundHandler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SendBytes;
import com.wincom.protocol.modbus.ModbusFrame;
import java.nio.ByteBuffer;

/**
 *
 * @author master
 */
public class ModbusOutboundHandlerImpl extends ChannelOutboundHandler.Adapter {
    
    private HandlerContext outboundContext;
    
    @Override
    public void handleSendPayload(HandlerContext ctx, Message m) {
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
