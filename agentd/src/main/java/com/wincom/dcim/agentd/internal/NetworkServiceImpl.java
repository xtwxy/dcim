package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import io.netty.util.concurrent.DefaultThreadFactory;
import java.util.concurrent.ThreadFactory;
import org.osgi.framework.BundleContext;

public final class NetworkServiceImpl implements NetworkService {

    private ThreadFactory threadFactory;
    private final EventLoopGroup eventLoopGroup;
    private HashedWheelTimer timer;
    
    public NetworkServiceImpl() {
        this(null);
    }
    public NetworkServiceImpl(BundleContext context) {
        this.threadFactory = new DefaultThreadFactory("agentd-thread-factory");

        int threads = 32;
        if (context != null) {
            String initialThreads = context.getProperty("initial.threads");
            if (initialThreads != null) {
                try {
                    threads = Integer.parseInt(initialThreads);
                } catch (Exception ex) {
                    throw new RuntimeException(initialThreads, ex);
                }
            }
        }
        this.eventLoopGroup = new NioEventLoopGroup(threads, this.threadFactory);
        this.timer = new HashedWheelTimer(threadFactory);
    }

    @Override
    public ThreadFactory getThreadFactory() {
        return this.threadFactory;
    }

    @Override
    public EventLoopGroup getEventLoopGroup() {
        return this.eventLoopGroup;
    }

    @Override
    public HandlerContext createHandlerContext() {
        return new StreamHandlerContextImpl(this);
    }

    @Override
    public Timer getTimer() {
        return this.timer;
    }

}
