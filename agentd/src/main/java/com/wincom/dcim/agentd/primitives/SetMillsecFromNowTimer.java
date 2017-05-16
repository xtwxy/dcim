package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class SetMillsecFromNowTimer extends Message.Adapter {
    
    private final long millsec;
    
    public SetMillsecFromNowTimer(long millsec) {
        this.millsec = millsec;
    }

    public long getMillsec() {
        return millsec;
    }

    @Override
    public String toString() {
        return String.format("SetMillsecFromNowTimer %s millsec from now.", millsec);
    }
}
