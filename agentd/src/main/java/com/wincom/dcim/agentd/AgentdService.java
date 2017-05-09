package com.wincom.dcim.agentd;

import com.wincom.dcim.agentd.primitives.HandlerContext;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import java.util.concurrent.ThreadFactory;

public interface AgentdService {

    public ThreadFactory getThreadFactory();

    public EventLoopGroup getEventLoopGroup();

    public ChannelFuture createServerChannel(HandlerContext ctx, String host, int port);

    public ChannelFuture createClientChannel(HandlerContext ctx, String host, int port);

    public void registerCodecFactory(String key, CodecFactory factory);

    public void unregisterCodecFactory(String key);
    
    public HandlerContext createHandlerContext();
}
