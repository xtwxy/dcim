package com.wincom.fsu.mp3000.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.CodecChannel;
import com.wincom.dcim.agentd.Connector;
import com.wincom.dcim.agentd.Dependency;
import io.netty.channel.Channel;

public class MP3000CodecChannelImpl
        extends CodecChannel.Adapter
        implements Connector, Dependency {

    private String host;
    private int port;
    private AgentdService agent;

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
    }

    @Override
    public void write(Object msg) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                MP3000CodecChannelImpl.super.write(msg);
            }
        };
        getEventLoopGroup().submit(withDependencies(r));
    }

    @Override
    public Runnable withDependencies(Runnable target) {
        Runnable r = target;
        if (getChannel() == null || !getChannel().isOpen()) {
            // connect
            r = new Runnable() {
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
                    }
                    );
                }

            };
        }
        return r;
    }
}
