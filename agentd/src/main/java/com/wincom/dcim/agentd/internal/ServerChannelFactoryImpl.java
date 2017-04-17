package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.Acceptor;
import com.wincom.dcim.agentd.AgentdThreadFactory;
import com.wincom.dcim.agentd.ServerChannelFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import static java.lang.System.out;

public class ServerChannelFactoryImpl implements ServerChannelFactory {

    private BundleContext bundleContext;

    public ServerChannelFactoryImpl(BundleContext context) {
        this.bundleContext = context;
    }

    @Override
    public ChannelFuture create(final String host, final int port, final Acceptor acceptor) {
        out.println("host = " + host
                + ", port = " + port
                + ", acceptor = " + acceptor);

        ServiceReference<AgentdThreadFactory> threadFactoryRef = bundleContext.getServiceReference(AgentdThreadFactory.class);
        AgentdThreadFactory threadFactory = bundleContext.getService(threadFactoryRef);
        EventLoopGroup group = threadFactory.getEventLoopGroup();
        out.println(threadFactoryRef);
        out.println(threadFactory);
        out.println(group);

        ServerBootstrap boot = new ServerBootstrap();
        ServerBootstrap childOption = boot.group(group)
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
}
