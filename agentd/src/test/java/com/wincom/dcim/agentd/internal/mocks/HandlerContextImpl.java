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
    private final HandlerContext inbound;
    private final Map<Class, Handler> handlers;

    public HandlerContextImpl(HandlerContext inbound) {
        this.inbound = inbound;
        this.handlers = new HashMap<>();
        this.handlers.put(SendBytes.class, new WriteBytesHandlerImpl(inbound));
        this.handlers.put(Unknown.class, new DefaultHandlerImpl(inbound));
    }

    @Override
    public Handler getHandler(Class clazz) {
        Handler h = handlers.get(clazz);
        if (h == null) {
            h = handlers.get(Unknown.class);
        }
        return h;
    }
}
