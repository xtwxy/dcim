package com.wincom.dcim.agentd.internal.tests;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.internal.ChannelInboundHandler;
import com.wincom.dcim.agentd.internal.StreamHandlerContextImpl;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.dcim.agentd.primitives.Connected;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class ConnectState extends State.Adapter {

    Logger log = LoggerFactory.getLogger(this.getClass());

    protected final NetworkService service;
    protected final HandlerContext handlerContext;

    public ConnectState(NetworkService service, HandlerContext handlerContext) {
        this.service = service;
        this.handlerContext = handlerContext;
    }

    @Override
    public State on(HandlerContext context, Message m) {
        if (m instanceof Connected) {
            Connected a = (Connected) m;

            log.info(String.format("Connection established: local: %s, remote:%s", 
                    a.getChannel().localAddress(), a.getChannel().remoteAddress()));
            
            final StreamHandlerContextImpl clientContext
                    = (StreamHandlerContextImpl) context;
            clientContext.setChannel(a.getChannel());

            a.getChannel().pipeline()
                    .addLast(new IdleStateHandler(0, 0, 2))
                    .addLast(new ChannelInboundHandler(context));
            
            context.onSendComplete(m);
            
            return success();
        } else {
            log.info("Connect failed." + m);
            return fail();
        }
    }

}
