package com.wincom.dcim.agentd.internal;

import java.util.Dictionary;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.primitives.Accepted;
import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.dcim.agentd.statemachine.StateBuilder;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import static java.lang.System.out;
import java.nio.ByteBuffer;
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

        final StateMachine ssm = new StateMachine();

        final State accept = new State.Adapter() {
            @Override
            public State enter() {
                service.createServerChannel("0.0.0.0", 9080, ssm);
                return this;
            }

            @Override
            public State on(HandlerContext context, Message m) {
                if (m instanceof Accepted) {
                    Accepted a = (Accepted) m;

                    final StateMachine csm = new StateMachine();
                    context.setStateMachine(csm);
                    StateBuilder connection = StateBuilder.initial().state(new State.Adapter() {
                        @Override
                        public State on(HandlerContext ctx, Message m) {
                            if (m instanceof BytesReceived) {
                                ctx.send(m);
                                BytesReceived br = (BytesReceived) m;
                                ByteBuffer buffer = br.getByteBuffer();
//                                while (buffer.hasRemaining()) {
//                                    out.printf("%02x ", 0xff & buffer.get());
//                                }
//                                out.println();
                            }
                            return this;
                        }
                    });
                    csm.buildWith(connection);
                    csm.enter();

                    a.getChannel().pipeline()
                            .addLast(new IdleStateHandler(6, 6, 6))
                            .addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                    ByteBuffer buffer = null;
                                    if (msg instanceof ByteBuffer) {
                                        buffer = (ByteBuffer) buffer;
                                        csm.on(context, new BytesReceived(buffer));
                                    } else if (msg instanceof ByteBuf) {
                                        ByteBuf buf = (ByteBuf) msg;
                                        buffer = buf.nioBuffer();

                                        csm.on(context, new BytesReceived(buffer));
            
                                        buf.release();
                                    } else {
                                        out.println("unknow msg");
                                    }

                                }

                                @Override
                                public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                    if (evt instanceof IdleStateEvent) {
                                        IdleStateEvent e = (IdleStateEvent) evt;
                                        if (null == e.state()) {
                                            out.println(e);
                                            ctx.writeAndFlush(Unpooled.wrappedBuffer("What happend?\n".getBytes()));
                                        } else {
                                            switch (e.state()) {
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
                                }
                            });

                    return this;
                } else {
                    return fail();
                }
            }
        };

        StateBuilder server = StateBuilder
                .initial().state(accept);
        server.fail().state(new State.Adapter() {
            @Override
            public State on(HandlerContext ctx, Message m) {
                out.println("Create server failed.");
                return success();
            }
        });

        ssm.buildWith(server);
        ssm.enter();
    }
}
