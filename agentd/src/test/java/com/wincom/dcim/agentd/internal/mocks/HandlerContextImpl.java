package com.wincom.dcim.agentd.internal.mocks;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.SendBytes;
import com.wincom.dcim.agentd.primitives.Unknown;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author master
 */
public class HandlerContextImpl extends HandlerContext.Adapter {

    private final Map<Class, Handler> handlers;

    public HandlerContextImpl() {
        this.handlers = new HashMap<>();
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
    public void initHandlers(HandlerContext outboundContext) {
        this.handlers.put(SendBytes.class, new WriteBytesHandlerImpl(outboundContext));
        this.handlers.put(Unknown.class, new DefaultHandlerImpl(outboundContext));
    }
}
