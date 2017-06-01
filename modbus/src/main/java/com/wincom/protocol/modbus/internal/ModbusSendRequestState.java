package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SendBytes;
import com.wincom.dcim.agentd.primitives.WriteComplete;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.protocol.modbus.ModbusFrame;
import java.nio.ByteBuffer;

/**
 *
 * @author master
 */
public class ModbusSendRequestState extends State.Adapter {

    final private ModbusFrame request;
    final private HandlerContext outbound;

    ModbusSendRequestState(ModbusFrame m, HandlerContext outbound) {
        this.request = m;
        this.outbound = outbound;
    }

    @Override
    public State enter(HandlerContext ctx) {
        // encode and send.
        ByteBuffer buffer = ByteBuffer.allocate(request.getWireLength());
        request.toWire(buffer);
        buffer.flip();
        outbound.send(new SendBytes(ctx, buffer));
        ctx.set(ModbusCodecImpl.MODBUS_REQUEST_KEY, request);
        return this;
    }

    @Override
    public State on(HandlerContext ctx, Message m) {

        if (m instanceof WriteComplete) {
            return success();
        } else {
            return this;
        }
    }
}
