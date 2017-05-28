package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.Failed;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.ReadTimeout;
import com.wincom.dcim.agentd.primitives.Timeout;
import com.wincom.dcim.agentd.primitives.Unknown;
import com.wincom.dcim.agentd.primitives.WriteTimeout;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class ChannelInboundHandler extends ChannelInboundHandlerAdapter {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private final HandlerContext clientContext;

    public ChannelInboundHandler(HandlerContext clientContext) {
        this.clientContext = clientContext;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        clientContext.fire(new ChannelActive(clientContext));
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        clientContext.fire(new ChannelInactive(clientContext));
        ctx.fireChannelInactive();
        ctx.pipeline().close();
        ctx.channel().close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuffer buffer = null;
        if (msg instanceof ByteBuffer) {
            buffer = (ByteBuffer) buffer;
            clientContext.fire(new BytesReceived(clientContext, buffer));
        } else if (msg instanceof ByteBuf) {
            ByteBuf buf = (ByteBuf) msg;
            buffer = buf.nioBuffer();

            clientContext.fire(new BytesReceived(clientContext, buffer));

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
                        clientContext.fire(new ReadTimeout(clientContext));
                        break;
                    case WRITER_IDLE:
                        clientContext.fire(new WriteTimeout(clientContext));
                        break;
                    case ALL_IDLE:
                        clientContext.fire(new ChannelTimeout(clientContext));
                        break;
                    default:
                        clientContext.fire(new ChannelTimeout(clientContext));
                        break;
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        clientContext.fire(new Failed(cause));
        ctx.close();
    }
}
