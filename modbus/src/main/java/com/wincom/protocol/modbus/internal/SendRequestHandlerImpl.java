package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SendBytes;
import com.wincom.protocol.modbus.ModbusFrame;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class SendRequestHandlerImpl implements Handler {

    Logger log = LoggerFactory.getLogger(this.getClass());
    private final HandlerContext outbound;

    SendRequestHandlerImpl(HandlerContext outbound) {
        this.outbound = outbound;
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        final ModbusFrame frame = (ModbusFrame) m;
        ByteBuffer buffer = ByteBuffer.allocate(frame.getWireLength());
        frame.toWire(buffer);
        buffer.flip();
        
        outbound.send(new SendBytes(buffer), ctx);
    }
}
