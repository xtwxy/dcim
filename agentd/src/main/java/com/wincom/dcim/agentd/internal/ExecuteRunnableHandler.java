package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.messages.ExecuteRunnable;
import com.wincom.dcim.agentd.messages.Handler;
import com.wincom.dcim.agentd.messages.Message;

/**
 *
 * @author master
 */
public class ExecuteRunnableHandler implements Handler {

    private final NetworkService service;

    public ExecuteRunnableHandler(NetworkService service) {
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
