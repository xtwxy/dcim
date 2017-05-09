package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.CodecFactory;
import com.wincom.dcim.agentd.primitives.Accepted;
import com.wincom.dcim.agentd.primitives.Connected;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import org.osgi.framework.BundleContext;

public final class AgentdServiceImpl implements AgentdService {

    private BundleContext bundleContext;
    private ThreadFactory threadFactory;
    private EventLoopGroup eventLoopGroup;
    private Map<String, CodecFactory> codecFactories;

    public AgentdServiceImpl(BundleContext context) {
        this.bundleContext = context;
        this.threadFactory = new DefaultThreadFactory("agentd-thread-factory");

        int threads = 8;
        String initialThreads = context.getProperty("initial.threads");
        if (initialThreads != null) {
            try {
                threads = Integer.parseInt(initialThreads);
            } catch (Exception ex) {
                throw new RuntimeException(initialThreads, ex);
            }
        }
        this.eventLoopGroup = new NioEventLoopGroup(threads, this.threadFactory);
        this.codecFactories = new HashMap<>();
    }

    @Override
    public ThreadFactory getThreadFactory() {
        return this.threadFactory;
    }

    @Override
    public EventLoopGroup getEventLoopGroup() {
        return this.eventLoopGroup;
    }

    @Override
    public ChannelFuture createServerChannel(HandlerContext ctx, String host, int port) {
        ServerBootstrap boot = new ServerBootstrap();
        boot
                .group(this.eventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        StreamHandlerContextImpl impl = (StreamHandlerContextImpl) ctx;
                        impl.setChannel(ch);
                        ctx.fire(new Accepted(ch));
                    }

                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        try {
            return boot.bind(host, port).sync().channel().closeFuture();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ChannelFuture createClientChannel(HandlerContext ctx, String host, int port) {
        Bootstrap boot = new Bootstrap();
        boot
                .group(this.eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        StreamHandlerContextImpl impl = (StreamHandlerContextImpl) ctx;
                        impl.setChannel(ch);
                        ctx.fire(new Connected(ch));
                    }

                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.SO_KEEPALIVE, true);
        return boot.connect(host, port);
    }

    @Override
    public void registerCodecFactory(String key, CodecFactory factory) {
        this.codecFactories.put(key, factory);
    }

    @Override
    public void unregisterCodecFactory(String key) {
        this.codecFactories.remove(key);
    }

    @Override
    public HandlerContext createHandlerContext() {
        return new StreamHandlerContextImpl(this);
    }

    
}
