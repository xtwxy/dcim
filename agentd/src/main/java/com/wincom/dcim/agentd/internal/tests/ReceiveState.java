package com.wincom.dcim.agentd.internal.tests;

import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.ChannelActive;
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
    protected final HandlerContext handlerContext;
    byte[] ba = new byte[128];

    public ReceiveState(HandlerContext handlerContext) {
        this.handlerContext = handlerContext;

        for (int i = 0; i < ba.length; ++i) {
            ba[i] = (byte) (0xff & (i % 10 + '0'));
        }
    }

    @Override
    public State on(HandlerContext ctx, Message m) {
        if (m instanceof BytesReceived) {
            // echo back the bytes.
            ctx.send(new SendBytes(((BytesReceived) m).getByteBuffer()));
            return this;
       } else if (m instanceof WriteComplete) {
            ctx.onSendComplete(m);
            return success();
        } else if (m instanceof ChannelTimeout) {
            return this;
        } else if (m instanceof ChannelActive) {
            sendBytes(ctx);
            return this;
        } else if (m instanceof ConnectionClosed) {
            ctx.onSendComplete(m);
            return fail();
        } else {
            log.warn("unknown message: " + m);
            ctx.send(new CloseConnection());
            ctx.onSendComplete(m);
            return success();
        }
    }

    private void sendBytes(HandlerContext ctx) {
        ByteBuffer buffer = ByteBuffer.wrap(ba);
        ctx.send(new SendBytes(buffer));
    }

    @Override
    public String toString() {
        return "ReceiveState@" + this.hashCode();
    }
}
