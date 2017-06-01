package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.primitives.GetSignalValues.Response;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.Unknown;
import com.wincom.dcim.agentd.statemachine.SendMessageState;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.driver.dds3366d.internal.primitives.ReadSettings;
import com.wincom.driver.dds3366d.internal.primitives.ReadStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author master
 */
public abstract class DDS3366DHandlerContextImpl extends HandlerContext.Adapter {

    private final Map<Class, Handler> handlers;
    private HandlerContext outboundContext;

    public DDS3366DHandlerContextImpl(HandlerContext outboundContext) {
        this.handlers = new HashMap<>();
        this.outboundContext = outboundContext;
        activate(outboundContext);
    }

    @Override
    public void send(Message m) {
        SendMessageState s = new SendMessageState(m);
        if (m.isOob()) {
            sendImmediate(s);
        } else {
            enqueueForSendWhenActive(s);
        }
    }

    @Override
    public void send(Message m, HandlerContext reply) {
        final ArrayList<State> l = new ArrayList<>();
        final Response response = new Response();
        final HandlerContext forked = new HandlerContext.NullContext() {
            @Override
            public void fire(Message m) {
                if (m instanceof ReadStatus.Response
                        || m instanceof ReadSettings.Response) {
                    m.apply(reply, new Handler() {
                        @Override
                        public void handle(HandlerContext ctx, Message m) {
                            Response r = (Response) m;
                            response.getValues().putAll(r.getValues());
                        }
                    });
                    for (State s : l) {
                        if (!s.stopped()) {
                            return;
                        }
                    }
                    reply.fire(response);
                }
            }
        };
        l.add(ReadStatus.initial(m, forked));
        l.add(ReadSettings.initial(m, forked));
    }

    @Override
    public Handler getHandler(Class clazz) {
        Handler h = handlers.get(clazz);
        if (h == null) {
            h = handlers.get(Unknown.class);
        }
        return h;
    }

    @Override
    final public void activate(HandlerContext outboundContext) {
        this.handlers.put(Unknown.class, new DefaultHandlerImpl(outboundContext));
    }

    public HandlerContext getOutboundContext() {
        return outboundContext;
    }
}
