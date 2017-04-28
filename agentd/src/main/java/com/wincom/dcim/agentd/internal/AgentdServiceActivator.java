package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.Acceptor;
import java.util.Dictionary;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.wincom.dcim.agentd.AgentdService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
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
                ch.pipeline()
                        .addLast(new IdleStateHandler(6, 6, 6))
                        .addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                ctx.writeAndFlush(msg);
                            }

                            @Override
                            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                if (evt instanceof IdleStateEvent) {
                                    IdleStateEvent e = (IdleStateEvent) evt;
                                    if (null == e.state()) {
                                        out.println(e);
                                        ctx.writeAndFlush(Unpooled.wrappedBuffer("What happend?\n".getBytes()));
                                    } else switch (e.state()) {
                                        case READER_IDLE:
                                            ctx.writeAndFlush(Unpooled.wrappedBuffer("Hello? Anyone there?\n".getBytes()));
                                            break;
                                        case WRITER_IDLE:
                                            ctx.writeAndFlush(Unpooled.wrappedBuffer("I'd lost for words.\n".getBytes()));
                                            break;
                                        case ALL_IDLE:
                                            ctx.writeAndFlush(Unpooled.wrappedBuffer("Let's talk!\n".getBytes()));
                                            break;
                                        default:
                                            out.println(e);
                                            ctx.writeAndFlush(Unpooled.wrappedBuffer("What happend?\n".getBytes()));
                                            break;
                                    }
                                }
                            }
                        });
            }

        });
    }
}
