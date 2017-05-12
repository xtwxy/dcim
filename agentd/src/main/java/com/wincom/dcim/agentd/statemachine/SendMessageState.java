package com.wincom.dcim.agentd.statemachine;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;

/**
 *
 * @author master
 */
public class SendMessageState extends State.Adapter {

    final private Message message;
    final private Handler request;
    final private HandlerContext reply;

    public SendMessageState(Message m, Handler request) {
        this.message = m;
        this.request = request;
        this.reply = new HandlerContext.NullContext();
    }

    public SendMessageState(Message m, Handler request, HandlerContext reply) {
        this.message = m;
        this.request = request;
        this.reply = reply;
    }

    @Override
    public State enter(HandlerContext ctx) {
        request.handle(ctx, message);
        return this;
    }

    @Override
    public State on(HandlerContext ctx, Message m) {
        reply.fire(m);
        return this;
    }

}
