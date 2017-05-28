package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public interface ChannelInboundHandler extends Handler {
    public void handleAccepted(HandlerContext ctx, Accepted m);
    public void handleConnected(HandlerContext ctx, Connected m);
    public void handleChannelActive(HandlerContext ctx, ChannelActive m);
    public void handleChannelInactive(HandlerContext ctx, ChannelInactive m);
    public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m);
    public void handleConnectionClosed(HandlerContext ctx, ConnectionClosed m);
    public void handlePayloadReceived(HandlerContext ctx, Message m);
    public void handlePayloadSent(HandlerContext ctx, Message m);
}
