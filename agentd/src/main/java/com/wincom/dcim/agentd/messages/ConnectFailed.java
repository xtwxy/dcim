package com.wincom.dcim.agentd.messages;

import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public final class ConnectFailed extends SystemError {

    public ConnectFailed(HandlerContext c, Throwable cause) {
        super(c, cause);
    }

    @Override
    public String toString() {
        return String.format("%s@%d", getClass().getSimpleName(), hashCode());
    }
}
