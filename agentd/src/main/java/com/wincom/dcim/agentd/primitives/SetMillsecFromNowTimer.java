package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class SetMillsecFromNowTimer implements Message {
    
    private final long millsec;
    
    public SetMillsecFromNowTimer(long millsec) {
        this.millsec = millsec;
    }

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        handler.handle(ctx, this);
    }

    public long getMillsec() {
        return millsec;
    }

    @Override
    public String toString() {
        return String.format("SetMillsecFromNowTimer %s millsec from now.", millsec);
    }
}