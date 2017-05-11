package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.primitives.Failed;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SendBytes;
import com.wincom.dcim.agentd.primitives.WriteComplete;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class SendBytesHandler implements Handler {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private final Channel channel;
    private final NetworkService service;

    public SendBytesHandler(Channel channel, NetworkService service) {
        this.channel = channel;
        this.service = service;
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        if (m instanceof SendBytes) {
            ChannelPromise cp = channel.newPromise();
            cp.addListener(new GenericFutureListener() {
                @Override
                public void operationComplete(Future f) throws Exception {
                    if (f.isSuccess()) {
                        ctx.fire(new WriteComplete());
                    } else {
                        ctx.fire(new Failed(f.cause()));
                    }
                }
            });
            ByteBuf buf = Unpooled.wrappedBuffer(((SendBytes) m).getByteBuffer());

            channel.writeAndFlush(buf, cp);
        }
    }

}
