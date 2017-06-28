package com.wincom.dcim.agentd;

import io.netty.channel.EventLoopGroup;
import io.netty.util.Timer;
import java.util.concurrent.ThreadFactory;

public interface NetworkService {

    Timer getTimer();
    ThreadFactory getThreadFactory();

    EventLoopGroup getEventLoopGroup();

    HandlerContext createStreamHandlerContext();
}
