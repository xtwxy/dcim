package com.wincom.dcim.agentd;

public interface CodecChannel {
    /* Calls */
    public void write(Object msg);
    public void timeout();
    public void error(Exception e);
    public void close();
    public void execute(Runnable r);
    
    /* Callbacks */
    public void fireWriteComplete();
    public void fireRead(Object msg);
    public void fireClosed();
    public void fireTimeout();
    public void fireError(Exception e);
    public void fireExecute();
}
