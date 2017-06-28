package com.wincom.dcim.agentd.internal.tests;

import com.wincom.dcim.agentd.messages.BytesReceived;
import com.wincom.dcim.agentd.messages.ChannelInactive;
import com.wincom.dcim.agentd.messages.ChannelTimeout;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.messages.SendBytes;
import com.wincom.dcim.agentd.messages.WriteComplete;
import com.wincom.dcim.agentd.statemachine.State;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class ReceiveState extends State.Adapter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final byte[] ba = new byte[256];

    public ReceiveState() {
        for (int i = 0; i < ba.length; ++i) {
            ba[i] = (byte) (0xff & (i % 10 + '0'));
        }
    }

    @Override
    public State on(HandlerContext ctx, Message m) {
        if (m instanceof BytesReceived) {
            // echo back the bytes.
            ctx.send(new SendBytes(ctx, ((BytesReceived) m).getByteBuffer()));
            return success();
        } else if (m instanceof WriteComplete) {
            // sendBytes(ctx);
            return success();
        } else if (m instanceof ChannelTimeout) {
            sendBytes(ctx);
            return success();
        } else if (m instanceof ChannelInactive) {
            ctx.onClosed(m);
            return error();
        } else {
            log.warn(String.format("unknown message: %s , send CloseConnection", m));
            return success();
        }
    }

    private void sendBytes(HandlerContext ctx) {
        ctx.send(new SendBytes(ctx, ByteBuffer.wrap(ba)));
    }

    @Override
    public String toString() {
        return "ReceiveState@" + this.hashCode();
    }
}
