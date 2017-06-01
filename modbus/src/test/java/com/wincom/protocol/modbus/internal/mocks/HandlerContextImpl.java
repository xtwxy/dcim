package com.wincom.protocol.modbus.internal.mocks;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.HandlerContext;
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
}
