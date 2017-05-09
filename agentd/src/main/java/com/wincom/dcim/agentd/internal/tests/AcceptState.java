package com.wincom.dcim.agentd.internal.tests;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.primitives.Accepted;
import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.dcim.agentd.statemachine.StateBuilder;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import com.wincom.dcim.agentd.statemachine.nettyimpl.HandlerContextImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import static java.lang.System.out;
import java.nio.ByteBuffer;

/**
 *
 * @author master
 */
public class AcceptState extends State.Adapter {
    private final AgentdService service;
    private final HandlerContext handlerContext;
    public AcceptState(AgentdService service, HandlerContext handlerContext) {
        this.service = service;
        this.handlerContext = handlerContext;
    }
    
    @Override
    public State enter() {
        service.createServerChannel(handlerContext, "0.0.0.0", 9080);
        return this;
    }

    @Override
    public State on(HandlerContext context, Message m) {
        if (m instanceof Accepted) {
            Accepted a = (Accepted) m;

            StateBuilder connection = StateBuilder.initial().state(new State.Adapter() {
                @Override
                public State on(HandlerContext ctx, Message m) {
                    if (m instanceof BytesReceived) {
                        ctx.send(m);
                        BytesReceived br = (BytesReceived) m;
                        ByteBuffer buffer = br.getByteBuffer();
//                        while (buffer.hasRemaining()) {
//                            out.printf("%02x ", 0xff & buffer.get());
//                        }
//                        out.println();
                    }
                    return this;
                }
            });
            final HandlerContext clientContext
                    = new HandlerContextImpl(
                            new StateMachine(connection),
                            a.getChannel(),
                            service.getEventLoopGroup()
                    );

            a.getChannel().pipeline()
                    .addLast(new IdleStateHandler(0, 0, 6))
                    .addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) {
                            ByteBuffer buffer = null;
                            if (msg instanceof ByteBuffer) {
                                buffer = (ByteBuffer) buffer;
                                clientContext.fire(new BytesReceived(buffer));
                            } else if (msg instanceof ByteBuf) {
                                ByteBuf buf = (ByteBuf) msg;
                                buffer = buf.nioBuffer();

                                clientContext.fire(new BytesReceived(buffer));

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

}
