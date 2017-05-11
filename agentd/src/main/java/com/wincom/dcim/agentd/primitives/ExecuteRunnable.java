package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class ExecuteRunnable implements Message {

    private final Runnable runnable;

    public ExecuteRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        handler.handle(ctx, this);
    }

    public Runnable getRunnable() {
        return runnable;
    }

    @Override
    public String toString() {
        return String.format("ExecuteRunnable %s", runnable);
    }
}
