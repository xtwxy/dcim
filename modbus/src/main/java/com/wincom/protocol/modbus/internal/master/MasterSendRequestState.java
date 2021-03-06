package com.wincom.protocol.modbus.internal.master;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.ApplicationFailure;
import com.wincom.dcim.agentd.messages.ChannelInactive;
import com.wincom.dcim.agentd.messages.ChannelTimeout;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.messages.SendBytes;
import com.wincom.dcim.agentd.messages.SystemError;
import com.wincom.dcim.agentd.messages.WriteComplete;
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
            ctx.onRequestCompleted();
            return failure();
        } else if (m instanceof SystemError) {
            ctx.onRequestCompleted();
            return error();
        } else {
            return this;
        }
    }
}
