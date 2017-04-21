package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.Acceptor;
import java.util.Dictionary;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.wincom.dcim.agentd.AgentdService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import static java.lang.System.out;
import java.util.Properties;
import org.osgi.framework.ServiceReference;

public final class AgentdServiceActivator implements BundleActivator {

    @Override
    public void start(BundleContext bc) throws Exception {
        Dictionary props = new Properties();

        bc.addServiceListener(new ServiceListenerImpl());

        bc.registerService(AgentdService.class, new AgentdServiceImpl(bc), props);
        testServerChannelFactory(bc);
    }

    @Override
    public void stop(BundleContext bc) throws Exception {

    }

    private void testServerChannelFactory(BundleContext bundleContext) {
        ServiceReference<AgentdService> serviceRef = bundleContext.getServiceReference(AgentdService.class);
        AgentdService service = bundleContext.getService(serviceRef);
        out.println(serviceRef);
        out.println(service);
        service.createServerChannel("0.0.0.0", 9080, new Acceptor() {
            @Override
            public void onAccept(SocketChannel ch) {
                out.println("connection from: " + ch.remoteAddress());
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) {
                        ctx.writeAndFlush(msg);
                    }
                });
            }

        });
    }
}
