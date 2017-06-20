package com.wincom.dcim.connector.internal;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.connector.Connection;
import com.wincom.dcim.connector.ConnectionCallback;
import com.wincom.dcim.connector.ConnectionFactory;
import com.wincom.dcim.connector.internal.redisimpl.ConnectionImpl;
import com.wincom.dcim.connector.redisimpl.RedisClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.redis.RedisArrayAggregator;
import io.netty.handler.codec.redis.RedisBulkStringAggregator;
import io.netty.handler.codec.redis.RedisDecoder;
import io.netty.handler.codec.redis.RedisEncoder;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Internal implementation of our example OSGi service
 */
public final class ConnectionFactoryImpl
        implements ConnectionFactory {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String HOST = System.getProperty("host", "127.0.0.1");
    private static final int PORT = Integer.parseInt(System.getProperty("port", "6379"));
    private static final int MAX_CONNECTION_COUNT = 1000;
    private final NetworkService service;
    private final List<Connection> available;
    private final List<Connection> used;

    public ConnectionFactoryImpl(NetworkService service) {
        this.service = service;
        this.available = new LinkedList<>();
        this.used = new LinkedList<>();
    }

    @Override
    public synchronized void getConnection(final ConnectionCallback cc) {
        Connection c = null;
        if(available.isEmpty()) {
            try {
                newConnection(new ConnectionCallback() {
                    @Override
                    public void completed(Throwable cause, Connection c) {
                        used.add(c);
                        cc.completed(cause, c);
                    }
                });
            } catch (InterruptedException ie) {
                cc.completed(ie, c);
            }
        } else {
            c = available.remove(0);
            used.add(c);
            cc.completed(null, c);
        }
    }

    public void newConnection(ConnectionCallback cc) throws InterruptedException {

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
                        p.addLast(new RedisClientHandler());
                    }
                });
        b.connect(HOST, PORT).addListener(new GenericFutureListener<ChannelFuture>() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    cc.completed(null, new ConnectionImpl(future.channel(),ConnectionFactoryImpl.this));
                } else {
                    cc.completed(future.cause(), null);
                    log.info(future.cause().toString());
                }
            }
        });
    }

    @Override
    public synchronized void release(Connection c) {
        used.remove(c);
        if(available.size() < MAX_CONNECTION_COUNT) {
            available.add(c);
        } else {
            c.close();
        }
    }
}

