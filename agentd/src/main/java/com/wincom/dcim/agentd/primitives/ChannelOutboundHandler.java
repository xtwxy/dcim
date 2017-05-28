package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public interface ChannelOutboundHandler extends Handler {
    public void handleAccept(HandlerContext ctx, Accept m);
    public void handleConnect(HandlerContext ctx, Connect m);
    public void handleClose(HandlerContext ctx, CloseConnection m);
    public void handleSendPayload(HandlerContext ctx, SendBytes m);
}
