package com.wincom.dcim.agentd;

/**
 *
 * @author master
 */
public interface IoCompletionNotifier {

    public void fireRead(Object msg);

    public void fireClosed();

    public void fireTimeout();

    public void fireError(Exception e);

    public void fireComplete();

}
