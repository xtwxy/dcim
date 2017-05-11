package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class SetOnetimeTimer implements Message {
    
    private final int millsec;
    
    public SetOnetimeTimer(int millsec) {
        this.millsec = millsec;
    }

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        handler.handle(ctx, this);
    }

    public int getMillsec() {
        return millsec;
    }
}
