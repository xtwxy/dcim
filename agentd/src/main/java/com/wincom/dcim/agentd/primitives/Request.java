package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public abstract class Request {

    private final MessageType type;

    public Request(MessageType type) {
        this.type = type;
    }
}
