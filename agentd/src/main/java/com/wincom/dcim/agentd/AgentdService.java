package com.wincom.dcim.agentd;

import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import java.util.concurrent.ThreadFactory;

public interface AgentdService {

    public ThreadFactory getThreadFactory();

    public EventLoopGroup getEventLoopGroup();

    public ChannelFuture createServerChannel(String host, int port, Acceptor acceptor);

    public ChannelFuture createClientChannel(String host, int port, Connector connector);

    public void registerCodecFactory(String key, CodecFactory factory);

    public void unregisterCodecFactory(String key);
}
