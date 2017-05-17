package com.wincom.dcim.agentd.internal.tests;

import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.CloseConnection;
import com.wincom.dcim.agentd.primitives.ConnectionClosed;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SendBytes;
import com.wincom.dcim.agentd.primitives.WriteComplete;
import com.wincom.dcim.agentd.statemachine.State;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class ReceiveState extends State.Adapter {

    Logger log = LoggerFactory.getLogger(this.getClass());

    public ReceiveState() {
    }

    @Override
    public State on(HandlerContext ctx, Message m) {
        if (m instanceof BytesReceived) {
            // echo back the bytes.
            ctx.send(new SendBytes(((BytesReceived) m).getByteBuffer()));
            return success();
        } else if (m instanceof WriteComplete) {
            // sendBytes(ctx);
            //ctx.onSendComplete(m);
            return success();
        } else if (m instanceof ChannelTimeout) {
            ctx.printState(m);
            sendBytes(ctx);
            return success();
        } else if (m instanceof ChannelActive) {
            ctx.setActive(true);
            sendBytes(ctx);
            return success();
        } else if (m instanceof ChannelInactive) {
            ctx.printState(m);
            ctx.fireClosed(m);
            return fail();
        } else if (m instanceof ConnectionClosed) {
            ctx.printState(m);
            ctx.fireClosed(m);
            return fail();
        } else {
            log.warn(String.format("unknown message: %s , send CloseConnection", m));
            ctx.send(new CloseConnection());
            return success();
        }
    }

    public void sendBytes(HandlerContext ctx) {
        byte[] ba = new byte[256];
        for (int i = 0; i < ba.length; ++i) {
            ba[i] = (byte) (0xff & (i % 10 + '0'));
        }
        ctx.send(new SendBytes(ByteBuffer.wrap(ba)));
    }

    @Override
    public String toString() {
        return "ReceiveState@" + this.hashCode();
    }
}
