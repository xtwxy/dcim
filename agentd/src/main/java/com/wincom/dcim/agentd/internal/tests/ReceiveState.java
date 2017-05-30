package com.wincom.dcim.agentd.internal.tests;

import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.ConnectFailed;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.MillsecFromNowTimeout;
import com.wincom.dcim.agentd.primitives.ReadTimeout;
import com.wincom.dcim.agentd.primitives.SendBytes;
import com.wincom.dcim.agentd.primitives.WriteComplete;
import com.wincom.dcim.agentd.primitives.WriteTimeout;
import com.wincom.dcim.agentd.statemachine.State;
import io.netty.util.Timeout;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class ReceiveState extends State.Adapter {

    Logger log = LoggerFactory.getLogger(this.getClass());
    byte[] ba = new byte[256];

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
            ctx.onRequestCompleted(m);
            return success();
        } else if (m instanceof WriteTimeout) {
            // sendBytes(ctx);
            return success();
        } else if (m instanceof ReadTimeout) {
            ctx.onRequestCompleted(m);
            return success();
        } else if (m instanceof ChannelTimeout) {
            ctx.onRequestCompleted(m);
            return success();
        } else if (m instanceof ChannelActive) {
            Object o = ctx.get("timeout");
            if(o instanceof Timeout) {
                Timeout t = (Timeout) o;
                t.cancel();
            }
            ctx.remove("timeout");
            ctx.setActive(true);
            
            sendBytes(ctx);
            
            return success();
        } else if (m instanceof ChannelInactive) {
            ctx.fireClosed(m);
            return fail();
        } else if (m instanceof ConnectFailed) {
            return fail();
        } else if (m instanceof MillsecFromNowTimeout) {
            ctx.remove("timeout");
            if (ctx.isActive()) {
                return success();
            } else {
                return fail();
            }
        } else {
            log.warn(String.format("unknown message: %s , send CloseConnection", m));
            return success();
        }
    }

    public void sendBytes(HandlerContext ctx) {
        ctx.send(new SendBytes(ctx, ByteBuffer.wrap(ba)));
    }

    @Override
    public String toString() {
        return "ReceiveState@" + this.hashCode();
    }
}
