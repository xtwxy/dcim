package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.primitives.Connect;
import com.wincom.dcim.agentd.primitives.Connected;
import com.wincom.dcim.agentd.primitives.ConnectionClosed;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import static java.lang.System.out;

/**
 *
 * @author master
 */
public class ConnectHandler implements Handler {

    private final Channel channel;
    private final NetworkService service;

    public ConnectHandler(Channel channel, NetworkService service) {
        this.channel = channel;
        this.service = service;
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        if (m instanceof Connect) {
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
                            ctx.fire(new Connected(ch));
                        }

                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);
            boot.connect(a.getHost(), a.getPort());
        }
    }
}
