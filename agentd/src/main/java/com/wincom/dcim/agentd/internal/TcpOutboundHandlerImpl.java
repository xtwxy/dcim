package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.primitives.Accept;
import com.wincom.dcim.agentd.primitives.Accepted;
import com.wincom.dcim.agentd.primitives.CloseConnection;
import com.wincom.dcim.agentd.primitives.Connect;
import com.wincom.dcim.agentd.primitives.ConnectFailed;
import com.wincom.dcim.agentd.primitives.Connected;
import com.wincom.dcim.agentd.primitives.ConnectionClosed;
import com.wincom.dcim.agentd.primitives.Failed;
import io.netty.channel.Channel;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SendBytes;
import com.wincom.dcim.agentd.primitives.WriteComplete;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wincom.dcim.agentd.ChannelOutboundHandler;
import com.wincom.dcim.agentd.TimerOutboundHandler;
import com.wincom.dcim.agentd.primitives.DeadlineTimeout;
import com.wincom.dcim.agentd.primitives.MillsecFromNowTimeout;
import com.wincom.dcim.agentd.primitives.SetDeadlineTimer;
import com.wincom.dcim.agentd.primitives.SetMillsecFromNowTimer;
import com.wincom.dcim.agentd.primitives.SetPeriodicTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author master
 */
public final class TcpOutboundHandlerImpl 
        extends ChannelOutboundHandler.Adapter 
        implements TimerOutboundHandler {

    private final Logger log;

    private Channel channel;
    private final NetworkService service;

    TcpOutboundHandlerImpl(NetworkService service) {
        this.log = LoggerFactory.getLogger(this.getClass());
        this.service = service;
    }

    public final void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void handleAccept(HandlerContext ctx, Accept a) {
        log.info(String.format("creating acceptor on %s:%d", a.getHost(), a.getPort()));
        ServerBootstrap boot = new ServerBootstrap();
        boot
                .group(service.getEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        StreamHandlerContextImpl impl = (StreamHandlerContextImpl) service.createHandlerContext();
                        impl.setChannel(ch);
                        ctx.fire(new Accepted(impl));
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        boot.bind(a.getHost(), a.getPort());
    }

    @Override
    public void handleConnect(HandlerContext ctx, Connect m) {
        Connect a = (Connect) m;

        Bootstrap boot = new Bootstrap();
        boot
                .group(service.getEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        StreamHandlerContextImpl impl = (StreamHandlerContextImpl) ctx;
                        impl.setChannel(ch);
                        ctx.fire(new Connected(ctx, ch));
                    }

                })
                .option(ChannelOption.SO_KEEPALIVE, true);
        ChannelFuture future = boot.connect(a.getHost(), a.getPort());
        future.addListener(new GenericFutureListener() {
            @Override
            public void operationComplete(Future f) throws Exception {
                if (f.isSuccess()) {
                    log.info(String.format("connection was successful: %s", f));
                } else {
                    ctx.fire(new ConnectFailed());
                    log.info(String.format("connection was failed: %s", f));
                }
            }

        });
    }

    @Override
    public void handleClose(HandlerContext ctx, CloseConnection m) {
        if (!ctx.isActive()) {
            ctx.fire(new Failed(ctx, new Exception("Not connected.")));
            return;
        }
        ChannelPromise cp = channel.newPromise();
        cp.addListener(new GenericFutureListener() {
            @Override
            public void operationComplete(Future f) throws Exception {
                if (f.isSuccess()) {
                    ctx.fire(new ConnectionClosed(channel));
                } else {
                    ctx.fire(new Failed(ctx, f.cause()));
                }
            }
        });
        channel.close(cp);
    }

    @Override
    public void handleSendPayload(HandlerContext ctx, Message m) {
        if (!ctx.isActive()) {
            ctx.fire(new Failed(ctx, new Exception("Not connected.")));
            return;
        }
        ChannelPromise cp = channel.newPromise();
        cp.addListener(new GenericFutureListener() {
            @Override
            public void operationComplete(Future f) throws Exception {
                if (f.isSuccess()) {
                    ctx.fire(new WriteComplete(ctx));
                } else {
                    ctx.fire(new Failed(ctx, f.cause()));
                }
            }
        });
        ByteBuf buf = Unpooled.wrappedBuffer(((SendBytes) m).getByteBuffer());

        channel.writeAndFlush(buf, cp);
    }

    @Override
    public void handleSetDeadlineTimer(HandlerContext ctx, SetDeadlineTimer m) {
        TimerTask tt = new TimerTask() {
            @Override
            public void run(Timeout tmt) throws Exception {
                ctx.fire(new DeadlineTimeout());
            }

        };
        long deadline = m.getTime().getTime();
        long now = System.currentTimeMillis();
        if (deadline > now) {
            service.getTimer().newTimeout(tt, (deadline - now), TimeUnit.MILLISECONDS);
        } else {
            ctx.fire(new DeadlineTimeout());
        }
    }

    @Override
    public void handleSetMillsecFromNowTimer(HandlerContext ctx, SetMillsecFromNowTimer m) {
        SetMillsecFromNowTimer setTimer = (SetMillsecFromNowTimer) m;
        TimerTask tt = new TimerTask() {
            @Override
            public void run(Timeout tmt) throws Exception {
                log.info("timout.");
                tmt.cancel();
                ctx.fire(new MillsecFromNowTimeout());
            }

        };
        service.getTimer().newTimeout(tt, setTimer.getMillsec(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void handleSetPeriodicTimer(HandlerContext ctx, SetPeriodicTimer m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
