package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.primitives.ExecuteRunnable;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import io.netty.channel.Channel;

/**
 *
 * @author master
 */
public class ExecuteRunnableHandler implements Handler {

    private final Channel channel;
    private final NetworkService service;

    public ExecuteRunnableHandler(Channel channel, NetworkService service) {
        this.channel = channel;
        this.service = service;
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        if (m instanceof ExecuteRunnable) {
            Runnable command = ((ExecuteRunnable) m).getRunnable();
            service.getEventLoopGroup().execute(command);
        }
    }

}
