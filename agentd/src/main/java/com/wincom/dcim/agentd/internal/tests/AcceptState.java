package com.wincom.dcim.agentd.internal.tests;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.primitives.Accepted;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.dcim.agentd.primitives.Accept;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class AcceptState extends State.Adapter {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private final NetworkService service;
    protected final HandlerContext handlerContext;
    protected final String host;
    protected final int port;

    public AcceptState(NetworkService service,
            HandlerContext handlerContext,
            String host,
            int port
    ) {
        this.service = service;
        this.handlerContext = handlerContext;
        this.host = host;
        this.port = port;

    }

    @Override
    public State enter(HandlerContext ctx) {
        this.handlerContext.send(new Accept(host, port));
        return this;
    }

    @Override
    public State on(HandlerContext context, Message m) {
        if (m instanceof Accepted) {
            Accepted a = (Accepted) m;

            return this;
        } else {
            return error();
        }
    }

    @Override
    public String toString() {
        return "AcceptState@" + this.hashCode();
    }
}
