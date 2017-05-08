package com.wincom.dcim.agentd.statemachine.nettyimpl;

import com.wincom.dcim.agentd.primitives.CloseConnection;
import com.wincom.dcim.agentd.primitives.ConnectionClosed;
import com.wincom.dcim.agentd.primitives.Failed;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 *
 * @author master
 */
public class CloseConnectionHandler implements Handler {

    private final Channel channel;
    private final EventLoopGroup eventLoopGroup;

    public CloseConnectionHandler(Channel channel, EventLoopGroup eventLoopGroup) {
        this.channel = channel;
        this.eventLoopGroup = eventLoopGroup;
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        if (m instanceof CloseConnection) {
            ChannelPromise cp = channel.newPromise();
            cp.addListener(new GenericFutureListener(){
                @Override
                public void operationComplete(Future f) throws Exception {
                    if(f.isSuccess()) {
                        ctx.fire(new ConnectionClosed(channel));
                    } else {
                        ctx.fire(new Failed(f.cause()));
                    }
                }
            });
            channel.close(cp);
        }
    }

}
