package com.wincom.dcim.agentd.statemachine;

import com.wincom.dcim.agentd.internal.ChannelInboundHandler;
import com.wincom.dcim.agentd.internal.StreamHandlerContextImpl;
import com.wincom.dcim.agentd.primitives.Connect;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.Connected;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class ConnectState extends State.Adapter {

    Logger log = LoggerFactory.getLogger(this.getClass());

    protected final HandlerContext handlerContext;
    protected final String host;
    protected final int port;

    public ConnectState(
            HandlerContext handlerContext,
            String host,
            int port
    ) {
        this.handlerContext = handlerContext;
        this.host = host;
        this.port = port;
    }

    @Override
    public State enter(HandlerContext ctx) {
        this.handlerContext.send(new Connect(host, port));
        return this;
    }

    @Override
    public State on(HandlerContext ctx, Message m) {
        if (m instanceof Connected) {
            Connected a = (Connected) m;

            log.info(String.format("Connection established: local: %s, remote:%s",
                    a.getChannel().localAddress(), a.getChannel().remoteAddress()));

            final StreamHandlerContextImpl clientContext
                    = (StreamHandlerContextImpl) ctx;
            clientContext.setChannel(a.getChannel());

            a.getChannel().pipeline()
                    .addLast(new LoggingHandler(LogLevel.INFO))
                    .addLast(new IdleStateHandler(20, 1, 20))
                    .addLast(new ChannelInboundHandler(ctx));

            ctx.onRequestCompleted(m);
            
            //ctx.send(new SetMillsecFromNowTimer(60000));

            return success();
        } else {
            log.warn(String.format("Unknown state: (%s, %s, %s)", this, ctx, m));
            return this;
        }
    }

    @Override
    public String toString() {
        return "ConnectState@" + this.hashCode();
    }
}
