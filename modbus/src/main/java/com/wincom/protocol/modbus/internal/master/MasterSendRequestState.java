package com.wincom.protocol.modbus.internal.master;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.ApplicationFailure;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SendBytes;
import com.wincom.dcim.agentd.primitives.SystemError;
import com.wincom.dcim.agentd.primitives.WriteComplete;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.protocol.modbus.ModbusFrame;
import java.nio.ByteBuffer;

/**
 *
 * @author master
 */
public class MasterSendRequestState extends State.Adapter {

    final private ModbusFrame request;
    final private HandlerContext outbound;
    
    MasterSendRequestState(ModbusFrame m, HandlerContext outbound) {
        this.request = m;
        this.outbound = outbound;
    }

    @Override
    public State enter(HandlerContext ctx) {
        // encode and send.
        ByteBuffer buffer = ByteBuffer.allocate(request.getWireLength());
        request.toWire(buffer);
        buffer.flip();
        ctx.set(MasterCodecImpl.MODBUS_REQUEST_KEY, request);
        outbound.send(new SendBytes(ctx, buffer));
        
        // return success() instead of this
        // because netty is half sync-half async.
        // in pure async mode, the state transition must be
        // executed after WriteComplete is fired.
        return success();
    }

    @Override
    public State on(HandlerContext ctx, Message m) {

        if (m instanceof WriteComplete) {
            return success();
        } else if (m instanceof ChannelInactive
                || m instanceof ChannelTimeout
                || m instanceof ApplicationFailure) {
            ctx.onRequestCompleted(m);
            return failure();
        } else if (m instanceof SystemError) {
            ctx.onRequestCompleted(m);
            return error();
        } else {
            return this;
        }
    }
}
