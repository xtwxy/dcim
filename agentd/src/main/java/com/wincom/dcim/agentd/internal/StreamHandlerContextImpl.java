package com.wincom.dcim.agentd.internal;

import com.google.common.annotations.VisibleForTesting;
import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.primitives.Accept;
import com.wincom.dcim.agentd.primitives.CloseConnection;
import com.wincom.dcim.agentd.primitives.Connect;
import com.wincom.dcim.agentd.primitives.ExecuteRunnable;
import com.wincom.dcim.agentd.primitives.Handler;
import io.netty.channel.Channel;
import java.util.HashMap;
import java.util.Map;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.SendBytes;
import com.wincom.dcim.agentd.primitives.SetDeadlineTimer;
import com.wincom.dcim.agentd.primitives.SetMillsecFromNowTimer;
import com.wincom.dcim.agentd.primitives.Unknown;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public final class StreamHandlerContextImpl extends HandlerContext.Adapter {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private Channel channel;
    private final NetworkService service;
    private final Map<Class, Handler> handlers;

    /**
     * This Constructor is *ONLY* called by internal implementations, call
     * <code>NetworkService#createHandlerContext()</code> instead.
     *
     * @param machine
     * @param channel
     * @param eventLoopGroup
     */
    @VisibleForTesting
    StreamHandlerContextImpl(StateMachine machine,
            Channel channel,
            NetworkService service
    ) {
        super(machine);
        this.service = service;
        this.handlers = new HashMap<>();
        setChannel(channel);
    }

    /**
     * This Constructor is *ONLY* called by internal implementations, call
     * <code>NetworkService#createHandlerContext()</code> instead.
     *
     * @param eventLoopGroup
     */
    @VisibleForTesting
    StreamHandlerContextImpl(
            NetworkService service
    ) {
        this(new StateMachine(), null, service);
    }

    private void initHandlers() {
        handlers.put(SendBytes.class, new SendBytesHandler(channel, service));
        handlers.put(CloseConnection.class, new CloseConnectionHandler(channel, service));
        handlers.put(ExecuteRunnable.class, new ExecuteRunnableHandler(channel, service));
        handlers.put(Accept.class, new AcceptHandler(channel, service));
        handlers.put(Connect.class, new ConnectHandler(channel, service));
        handlers.put(SetMillsecFromNowTimer.class, new SetMillsecFromNowTimerHandler(channel, service));
        handlers.put(SetDeadlineTimer.class, new SetDeadlineTimerHandler(channel, service));
        handlers.put(Unknown.class, new UnknownHandler(channel, service));
    }

    public final void setChannel(Channel channel) {
        this.channel = channel;
        initHandlers();
    }

    @Override
    public Handler getHandler(Class clazz) {
        Handler h = handlers.get(clazz);
        if (h != null) {
            return h;
        }
        return handlers.get(Unknown.class);
    }

    @Override
    public String toString() {
        return "StreamHandlerContextImpl@" + this.hashCode();
    }

    @Override
    public void initHandlers(HandlerContext outboundContext) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
