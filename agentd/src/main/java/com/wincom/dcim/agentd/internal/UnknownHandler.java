package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class UnknownHandler implements Handler {

    Logger log = LoggerFactory.getLogger(this.getClass());
    
    private final Channel channel;
    private final AgentdService service;

    public UnknownHandler(Channel channel, AgentdService service) {
        this.channel = channel;
        this.service = service;
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        log.warn("unknown message: " + m);
    }

}
