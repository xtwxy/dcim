/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wincom.dcim.agentd;

/**
 *
 * @author master
 */
public interface CodecChannel {
    /* Calls */
    public void write(Object msg);
    public void timeout();
    public void error(Exception e);
    public void close();
    public void execute(Runnable r);
    
    /* Callbacks */
    public void fireRead(Object msg);
    public void fireClosed();
    public void fireTimeout();
    public void fireError(Exception e);
    public void fireExecute();
}
