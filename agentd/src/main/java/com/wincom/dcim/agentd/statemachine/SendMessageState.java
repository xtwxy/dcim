package com.wincom.dcim.agentd.statemachine;

import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;

/**
 *
 * @author master
 */
public class SendMessageState extends State.Adapter {

    final private Message message;
    final private HandlerContext reply;

    public SendMessageState(Message m) {
        this.message = m;
        this.reply = new HandlerContext.NullContext();
    }

    public SendMessageState(Message m, HandlerContext reply) {
        this.message = m;
        this.reply = reply;
    }

    @Override
    public State enter(HandlerContext ctx) {
        ctx.getHandler(message.getClass()).handle(ctx, message);
        return this;
    }

    @Override
    public State on(HandlerContext ctx, Message m) {
        reply.fire(m);
        return this;
    }

    @Override
    public String toString() {
        return "SendMessageState@" + this.hashCode();
    }
}
