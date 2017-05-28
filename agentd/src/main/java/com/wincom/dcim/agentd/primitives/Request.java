package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public class Request {

    public final Message message;
    public final Handler handler;

    public Request(Message message, Handler handler) {
        this.message = message;
        this.handler = handler;
    }
}
