package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public interface Handler {
    public void apply(GetSignalValues.Request r);
    public void apply(GetSignalValues.Response r);
    public void apply(SetSignalValues.Request r);
    public void apply(SetSignalValues.Response r);
    public void apply(PushEvents pe);
}
