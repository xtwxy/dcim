package com.wincom.dcim.agentd.internal.tests;

import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.CloseConnection;
import com.wincom.dcim.agentd.primitives.ConnectionClosed;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.WriteComplete;
import com.wincom.dcim.agentd.statemachine.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class ReceiveState extends State.Adapter {

    Logger log = LoggerFactory.getLogger(this.getClass());
    protected final Handler handler;

    public ReceiveState(Handler handler) {
        this.handler = handler;
    }

    @Override
    public State on(HandlerContext ctx, Message m) {
        if (m instanceof BytesReceived) {
            // echo back the bytes.
            // ctx.send(new SendBytes(((BytesReceived) m).getByteBuffer()));
            handler.handle(ctx, m);
            return this;
       } else if (m instanceof WriteComplete) {
            ctx.onSendComplete(m);
            return success();
        } else if (m instanceof ChannelTimeout) {
            handler.handle(ctx, m);
            return this;
        } else if (m instanceof ChannelActive) {
            ctx.setActive(true);
            handler.handle(ctx, m);
            return this;
        } else if (m instanceof ChannelInactive) {
            ctx.setActive(false);
            handler.handle(ctx, m);
            return this;
        } else if (m instanceof ConnectionClosed) {
            ctx.onSendComplete(m);
            handler.handle(ctx, m);
            return fail();
        } else {
            log.warn("unknown message: " + m);
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
