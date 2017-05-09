package com.wincom.dcim.agentd.internal.tests;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.primitives.Accepted;
import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.dcim.agentd.statemachine.StateBuilder;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import com.wincom.dcim.agentd.internal.StreamHandlerContextImpl;
import com.wincom.dcim.agentd.primitives.ReadTimeout;
import com.wincom.dcim.agentd.primitives.Timeout;
import com.wincom.dcim.agentd.primitives.Unknown;
import com.wincom.dcim.agentd.primitives.WriteTimeout;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class AcceptState extends State.Adapter {

    Logger log = LoggerFactory.getLogger(this.getClass());
    
    private final NetworkService service;
    private final HandlerContext handlerContext;

    public AcceptState(NetworkService service, HandlerContext handlerContext) {
        this.service = service;
        this.handlerContext = handlerContext;
    }

    @Override
    public State on(HandlerContext context, Message m) {
        if (m instanceof Accepted) {
            Accepted a = (Accepted) m;
            
            log.info("Connection accepted: " + a.getChannel());
            
            StateBuilder connection = StateBuilder.initial().state(new State.Adapter() {
                @Override
                public State on(HandlerContext ctx, Message m) {
                    if (m instanceof BytesReceived) {
                        ctx.send(m);
                        BytesReceived br = (BytesReceived) m;
                        ByteBuffer buffer = br.getByteBuffer();
                    }
                    return this;
                }
            });
            final StreamHandlerContextImpl clientContext
                    = (StreamHandlerContextImpl) service.createHandlerContext();
            clientContext.setStateMachine(new StateMachine(connection));
            clientContext.setChannel(a.getChannel());
            
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
                                clientContext.fire(new Unknown(msg));
                            }

                        }

                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                            if (evt instanceof IdleStateEvent) {
                                IdleStateEvent e = (IdleStateEvent) evt;
                                if (null == e.state()) {
                                    clientContext.fire(new Timeout());
                                } else {
                                    switch (e.state()) {
                                        case READER_IDLE:
                                            clientContext.fire(new ReadTimeout());
                                            break;
                                        case WRITER_IDLE:
                                            clientContext.fire(new WriteTimeout());
                                            break;
                                        case ALL_IDLE:
                                            clientContext.fire(new Timeout());
                                            break;
                                        default:
                                            clientContext.fire(new Timeout());
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
