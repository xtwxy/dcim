package com.wincom.dcim.agentd.statemachine;

import com.wincom.dcim.agentd.messages.Handler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.Message;

/**
 *
 * @author master
 */
public class SendMessageState extends State.Adapter {

    final private Message message;
    final private Handler request;
    final private Handler reply;

    public SendMessageState(Message m, Handler request, Handler reply) {
        this.message = m;
        this.reply = request;
        this.request = reply;
    }

    @Override
    public State enter(HandlerContext ctx) {
        message.apply(ctx, request);
        return this;
    }

    @Override
    public State on(HandlerContext ctx, Message m) {
        m.apply(ctx, reply);
        return this;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("SendMessageState@%d, %s, %s",this.hashCode(), message, reply);
    }
}
