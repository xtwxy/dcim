package com.wincom.dcim.agentd;

import com.wincom.dcim.agentd.primitives.HandlerContext;
import io.netty.channel.EventLoopGroup;
import java.util.concurrent.ThreadFactory;

public interface NetworkService {

    public ThreadFactory getThreadFactory();

    public EventLoopGroup getEventLoopGroup();

    public HandlerContext createHandlerContext();
}
