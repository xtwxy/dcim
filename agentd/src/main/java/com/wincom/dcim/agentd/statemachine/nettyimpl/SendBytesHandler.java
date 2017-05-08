package com.wincom.dcim.agentd.statemachine.nettyimpl;

import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.Failed;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.WriteComplete;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import static java.lang.System.out;

/**
 *
 * @author master
 */
public class SendBytesHandler implements Handler {

    private final Channel channel;
    private final EventLoopGroup eventLoopGroup;

    public SendBytesHandler(Channel channel, EventLoopGroup eventLoopGroup) {
        this.channel = channel;
        this.eventLoopGroup = eventLoopGroup;
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        if (m instanceof BytesReceived) {
            ChannelPromise cp = channel.newPromise();
            cp.addListener(new GenericFutureListener(){
                @Override
                public void operationComplete(Future f) throws Exception {
                    if(f.isSuccess()) {
                        ctx.fire(new WriteComplete());
                    } else {
                        ctx.fire(new Failed(f.cause()));
                    }
                }
            });
            ByteBuf buf = Unpooled.wrappedBuffer(((BytesReceived) m).getByteBuffer());
            
            channel.writeAndFlush(buf, cp);
        }
    }

}
