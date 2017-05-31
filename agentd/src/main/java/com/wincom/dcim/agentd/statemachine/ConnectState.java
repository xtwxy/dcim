package com.wincom.dcim.agentd.statemachine;

import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.Connect;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.ConnectFailed;
import io.netty.util.Timeout;
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
        if (m instanceof ChannelActive) {
            Object o = ctx.get("timeout", null);
            if(o instanceof Timeout) {
                Timeout t = (Timeout) o;
                t.cancel();
            }
            ctx.remove("timeout");
            return success();
        } else if (m instanceof ConnectFailed) {
            return error();
        } else {
            log.warn(String.format("state: (%s, %s, %s)", this, ctx, m));
            return this;
        }
    }

    @Override
    public String toString() {
        return "ConnectState@" + this.hashCode();
    }
}
