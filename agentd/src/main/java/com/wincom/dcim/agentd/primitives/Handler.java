package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public interface Handler {
    public void handle(HandlerContext ctx, Message m);
}
