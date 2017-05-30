package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public final class ExecuteRunnable extends Message.Adapter {

    private final Runnable runnable;

    public ExecuteRunnable(Runnable runnable) {
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
