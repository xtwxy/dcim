package com.wincom.dcim.connector.internal;

import com.wincom.dcim.agentd.ChannelOutboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.messages.ApplicationFailure;
import com.wincom.dcim.agentd.messages.CloseConnection;
import com.wincom.dcim.agentd.messages.Connect;
import com.wincom.dcim.agentd.messages.ConnectFailed;
import com.wincom.dcim.agentd.messages.Connected;
import com.wincom.dcim.agentd.messages.ConnectionClosed;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.messages.SendString;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.redis.RedisArrayAggregator;
import io.netty.handler.codec.redis.RedisBulkStringAggregator;
import io.netty.handler.codec.redis.RedisDecoder;
import io.netty.handler.codec.redis.RedisEncoder;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * Created by master on 6/23/17.
 */
public class RedisClientOutboundHandlerImpl extends ChannelOutboundHandler.Adapter {

    private Channel channel;
    private final NetworkService service;

    public RedisClientOutboundHandlerImpl(NetworkService service) {
        this.service = service;
    }

    public final void setChannel(Channel channel) {
        this.channel = channel;
    }
    @Override
    public void handleConnect(HandlerContext ctx, Connect m) {
        Bootstrap b = new Bootstrap();
        b.group(service.getEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new RedisDecoder());
                        p.addLast(new RedisBulkStringAggregator());
                        p.addLast(new RedisArrayAggregator());
                        p.addLast(new RedisEncoder());
                        p.addLast(new RedisClientHandler(ctx));
                    }
                });
        b.connect(m.getHost(), m.getPort()).addListener(new GenericFutureListener<ChannelFuture>() {
            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (f.isSuccess()) {
                    RedisClientHandlerContextImpl impl = (RedisClientHandlerContextImpl) ctx;
                    impl.setChannel(f.channel());
                    m.getSender().fire(new Connected(ctx, f.channel()));
                    log.info("connection was successful: {}", f);
                } else {
                    ctx.fire(new ConnectFailed(ctx, f.cause()));
                    log.info("connection was failed: {}", f);
                }
            }
        });
    }

    @Override
    public void handleClose(HandlerContext ctx, CloseConnection m) {
        if (!ctx.isActive()) {
            ctx.fire(new ApplicationFailure(ctx, new Exception("Not connected.")));
            return;
        }
        ChannelPromise cp = channel.newPromise();
        cp.addListener(new GenericFutureListener<ChannelFuture>() {
            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (f.isSuccess()) {
                    ctx.fire(new ConnectionClosed(ctx, channel));
                } else {
                    ctx.fire(new ApplicationFailure(ctx, f.cause()));
                }
            }
        });
        channel.close(cp);
    }

    @Override
    public void handleSendPayload(HandlerContext ctx, Message m) {
        if (!ctx.isActive()) {
            ctx.fire(new ApplicationFailure(ctx, new Exception("Not connected.")));
            return;
        }

        channel.writeAndFlush(((SendString)m).getString());
        ctx.onRequestCompleted();
    }
}
