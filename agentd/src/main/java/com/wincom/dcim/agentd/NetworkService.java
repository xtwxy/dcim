package com.wincom.dcim.agentd;

import io.netty.channel.EventLoopGroup;
import io.netty.util.Timer;
import java.util.concurrent.ThreadFactory;

public interface NetworkService {

    public Timer getTimer();
    public ThreadFactory getThreadFactory();

    public EventLoopGroup getEventLoopGroup();

    public HandlerContext createHandlerContext();
}
