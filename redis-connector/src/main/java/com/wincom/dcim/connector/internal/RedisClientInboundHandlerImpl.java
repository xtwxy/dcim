package com.wincom.dcim.connector.internal;

import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.messages.*;
import io.netty.channel.Channel;

/**
 * Created by master on 6/23/17.
 */
public class RedisClientInboundHandlerImpl extends ChannelInboundHandler.Adapter {
    private Channel channel;
    private final NetworkService service;
    private final String PASSWORD;

    public RedisClientInboundHandlerImpl(NetworkService service, String password) {
        this.service = service;
        this.PASSWORD = password;
    }

    @Override
    public void handleConnected(HandlerContext ctx, Connected m) {
        ctx.onRequestCompleted();
    }

    @Override
    public void handleChannelActive(HandlerContext ctx, ChannelActive m) {
        ctx.send(new PasswordAuth(ctx, null, PASSWORD));
    }

    @Override
    public void handleChannelInactive(HandlerContext ctx, ChannelInactive m) {

    }

    @Override
    public void handleChannelReadTimeout(HandlerContext ctx, ReadTimeout m) {

    }

    @Override
    public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m) {

    }

    @Override
    public void handleConnectionClosed(HandlerContext ctx, ConnectionClosed m) {

    }

    @Override
    public void handlePayloadReceived(HandlerContext ctx, Message m) {

    }

    @Override
    public void handlePayloadSent(HandlerContext ctx, Message m) {

    }

    @Override
    public void handleApplicationFailure(HandlerContext ctx, ApplicationFailure m) {

    }

    @Override
    public void handleSystemError(HandlerContext ctx, SystemError m) {

    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
