package com.wincom.dcim.agentd.messages;

import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public final class ExecuteRunnable extends Message.Adapter {

    private final Runnable runnable;

    public ExecuteRunnable(HandlerContext sender, Runnable runnable) {
        super(sender);
        this.runnable = runnable;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    @Override
    public String toString() {
        return String.format("%s %s", getClass().getSimpleName(), runnable);
    }
}
