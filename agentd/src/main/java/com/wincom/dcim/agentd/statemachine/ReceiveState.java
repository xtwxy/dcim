package com.wincom.dcim.agentd.statemachine;

import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.MillsecFromNowTimeout;
import com.wincom.dcim.agentd.primitives.ReadTimeout;
import com.wincom.dcim.agentd.primitives.SendBytes;
import com.wincom.dcim.agentd.primitives.WriteComplete;
import com.wincom.dcim.agentd.primitives.WriteTimeout;
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
            // TODO: notify the inbound handlers.
            return success();
        } else if (m instanceof WriteComplete) {
            // TODO: notify the inbound handlers.
            ctx.onSendComplete(m);
            return success();
        } else if (m instanceof WriteTimeout) {
            return success();
        } else if (m instanceof ReadTimeout) {
            ctx.onSendComplete(m);
            return success();
        } else if (m instanceof ChannelTimeout) {
            ctx.onSendComplete(m);
            return success();
        } else if (m instanceof ChannelActive) {
            Object o = ctx.get("timeout");
            if(o instanceof Timeout) {
                Timeout t = (Timeout) o;
                t.cancel();
            }
            ctx.remove("timeout");
            ctx.setActive(true);
            
            // TODO: notify the inbound handlers.
            
            return success();
        } else if (m instanceof ChannelInactive) {
            ctx.setActive(false);
            ctx.close();
            ctx.fireClosed(m);
            return fail();
        } else if (m instanceof MillsecFromNowTimeout) {
            ctx.remove("timeout");
            if (ctx.isActive()) {
                return success();
            } else {
                ctx.close();
                return fail();
            }
        } else {
            log.warn(String.format("unknown message: %s , send CloseConnection", m));
            return success();
        }
    }

    public void sendBytes(HandlerContext ctx) {
        ctx.send(new SendBytes(ByteBuffer.wrap(ba)));
    }

    @Override
    public String toString() {
        return "ReceiveState@" + this.hashCode();
    }
}
