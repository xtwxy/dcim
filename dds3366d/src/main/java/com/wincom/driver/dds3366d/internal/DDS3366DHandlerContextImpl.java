package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Unknown;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author master
 */
public abstract class DDS3366DHandlerContextImpl extends HandlerContext.Adapter {

    private final Map<Class, Handler> handlers;
    HandlerContext outboundContext;

    public DDS3366DHandlerContextImpl(HandlerContext outboundContext) {
        this.handlers = new HashMap<>();
        this.outboundContext = outboundContext;
        initHandlers(outboundContext);
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
    final public void initHandlers(HandlerContext outboundContext) {
        this.handlers.put(Unknown.class, new DefaultHandlerImpl(outboundContext));
    }
}
