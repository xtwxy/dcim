package com.wincom.dcim.agentd.statemachine.nettyimpl;

import com.wincom.dcim.agentd.primitives.ExecuteRunnable;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;

/**
 *
 * @author master
 */
public class ExecuteRunnableHandler implements Handler {

    private final Channel channel;
    private final EventLoopGroup eventLoopGroup;

    public ExecuteRunnableHandler(Channel channel, EventLoopGroup eventLoopGroup) {
        this.channel = channel;
        this.eventLoopGroup = eventLoopGroup;
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        if (m instanceof ExecuteRunnable) {
            Runnable command = ((ExecuteRunnable) m).getRunnable();
            eventLoopGroup.execute(command);
        }
    }

}
