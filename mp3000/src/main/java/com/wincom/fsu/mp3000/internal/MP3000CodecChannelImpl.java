package com.wincom.fsu.mp3000.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.CodecChannel;
import com.wincom.dcim.agentd.Connector;
import com.wincom.dcim.agentd.ChainedDependency;
import com.wincom.dcim.agentd.IoCompletionHandler;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.dcim.agentd.statemachine.StateBuilder;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class MP3000CodecChannelImpl
        extends CodecChannel.Adapter
        implements Connector {

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
    public void write(Object msg, IoCompletionHandler handler) {
        ChainedDependency r = new ChainedDependency() {
            @Override
            public void run() {
                ChannelPromise cp = channel.voidPromise();
                cp.addListener(new GenericFutureListener() {
                    @Override
                    public void operationComplete(Future f) throws Exception {
                        if (f.isSuccess()) {
                            handler.onComplete();
                        } else {
                            handler.onError(new RuntimeException("Operation not successful: " + f.toString()));
                        }
                    }
                });
                channel.writeAndFlush(msg, cp);
            }
        };
        getEventLoopGroup().submit(withDependencies(r));
    }

    @Override
    public StateMachine withDependencies(StateMachine target) {
        if (getChannel() == null || !getChannel().isOpen()) {
            // connect
            StateBuilder builder = StateBuilder
                    .initial().state(new State.Adapter() {
                        @Override
                        public State enter() {
                            agent.createClientChannel(new Connector.Adapter() {
                                @Override
                                public void onConnect(Channel ch) {
                                    MP3000CodecChannelImpl.this.onConnect(ch);
                                }

                                @Override
                                public void onError(Exception e) {
                                    MP3000CodecChannelImpl.this.onError(e);
                                }
                            }, host,
                                    port);
                        }

                        @Override
                        public State on(Message m) {
                            return success();
                        }

                        @Override
                        public State exit() {

                        }
                    })
                    .success().state(target.initial());
            return new StateMachine(builder);
        }
        return target;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void onTimeout() {
    }

    @Override
    public void onError(Exception e) {
        this.channel = null;
    }

    @Override
    public void onClose() {
        this.channel = null;
    }

    @Override
    public void onComplete() {
        this.channel = null;
    }

}
