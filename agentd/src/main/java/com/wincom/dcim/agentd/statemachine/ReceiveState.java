package com.wincom.dcim.agentd.statemachine;

import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.CloseConnection;
import com.wincom.dcim.agentd.primitives.ConnectionClosed;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SendBytes;
import com.wincom.dcim.agentd.primitives.WriteComplete;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class ReceiveState extends State.Adapter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public ReceiveState() {
    }

    @Override
    public State on(HandlerContext ctx, Message m) {
        if (m instanceof BytesReceived) {
            // echo back the bytes.
            // ctx.send(new SendBytes(((BytesReceived) m).getByteBuffer()));
            return this;
        } else if (m instanceof WriteComplete) {
            ctx.onSendComplete(m);
            return success();
        } else if (m instanceof ChannelTimeout) {
            return this;
        } else if (m instanceof ChannelActive) {
            ctx.setActive(true);
            ctx.send(new SendBytes(ByteBuffer.wrap("Hello?".getBytes())));
            return this;
        } else if (m instanceof ChannelInactive) {
            ctx.setActive(false);
            return this;
        } else if (m instanceof ConnectionClosed) {
            ctx.onSendComplete(m);
            return fail();
        } else {
            log.warn(String.format("unknown message: %s , send CloseConnection", m));
            ctx.send(new CloseConnection());
            ctx.onSendComplete(m);
            return success();
        }
    }

    @Override
    public String toString() {
        return "ReceiveState@" + this.hashCode();
    }
}
