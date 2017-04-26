package com.wincom.fsu.mp3000.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.CodecChannel;
import com.wincom.dcim.agentd.Connector;
import com.wincom.dcim.agentd.Dependency;
import com.wincom.dcim.agentd.DependencyAdaptor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class MP3000CodecChannelImpl
        extends CodecChannel.Adapter
        implements Connector, Dependency {

    private final String host;
    private final int port;
    private final AgentdService agent;

    private Channel channel;

    public MP3000CodecChannelImpl(
            String host,
            int port,
            AgentdService agent
    ) {
        super(agent.getEventLoopGroup());
        this.host = host;
        this.port = port;
        this.agent = agent;
    }

    @Override
    public void onConnect(Channel ch) {
        if (getChannel() != null) {
            getChannel().close();
        }
        setChannel(ch);
        
        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                fireRead(msg);
            }
        });

    }

    @Override
    public void write(Object msg, Runnable promise) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                ChannelPromise cp = channel.voidPromise();
                cp.addListener(new GenericFutureListener() {
                    @Override
                    public void operationComplete(Future f) throws Exception {
                        promise.run();
                    }
                });
                channel.writeAndFlush(msg, cp);
            }
        };
        getEventLoopGroup().submit(withDependencies(r));
    }

    @Override
    public Runnable withDependencies(Runnable target) {
        Runnable r = target;
        if (getChannel() == null || !getChannel().isOpen()) {
            // connect
            r = new DependencyAdaptor(target) {
                @Override
                public void run() {
                    agent.createClientChannel(
                            host,
                            port,
                            new Connector() {
                        @Override
                        public void onConnect(Channel ch) {
                            MP3000CodecChannelImpl.this.onConnect(ch);
                            target.run();
                        }
                    });
                }
            };
        }
        return r;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

}
