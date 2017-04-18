package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.Acceptor;
import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Connector;
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
import java.util.concurrent.ThreadFactory;
import org.osgi.framework.BundleContext;

public final class AgentdServiceImpl implements AgentdService {

    private BundleContext bundleContext;
    private ThreadFactory threadFactory;
    private EventLoopGroup eventLoopGroup;
    
    public AgentdServiceImpl(BundleContext context) {
        this.bundleContext = context;
        this.threadFactory = new DefaultThreadFactory("agentd-thread-factory");
        
        int threads = 8;
        String initialThreads = context.getProperty("initial.threads");
        if(initialThreads != null) {
            try {
                threads = Integer.parseInt(initialThreads);
            } catch (Exception ex) {
                throw new RuntimeException(initialThreads, ex);
            }
        }
        this.eventLoopGroup = new NioEventLoopGroup(threads, this.threadFactory);
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
    public ChannelFuture createServerChannel(String host, int port, Acceptor acceptor) {
        ServerBootstrap boot = new ServerBootstrap();
        boot
                .group(this.eventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        acceptor.onAccept(ch);
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
    public ChannelFuture createClientChannel(String host, int port, Connector connector) {
        Bootstrap boot = new Bootstrap();
        boot
                .group(this.eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        connector.onConnect(ch);
                    }

                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .option(ChannelOption.SO_KEEPALIVE, true);
        try {
            return boot.connect(host, port).sync().channel().closeFuture();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

}
