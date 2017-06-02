package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public final class AcceptFailed extends SystemError {

    public AcceptFailed(HandlerContext c, Throwable cause) {
        super(c, cause);
    }

    @Override
    public String toString() {
        return String.format("%s@%d", getClass().getSimpleName(), hashCode());
    }
}
