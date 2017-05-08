package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class Failed implements Message {

    private Throwable cause;

    public Failed(Throwable cause) {
        this.cause = cause;
    }

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        handler.handle(ctx, this);
    }

    public Throwable getCause() {
        return cause;
    }
}
