/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.AgentdThreadFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import java.util.concurrent.ThreadFactory;
import org.osgi.framework.BundleContext;

/**
 *
 * @author master
 */
public class AgentdThreadFactoryImpl implements AgentdThreadFactory {

    ThreadFactory delegate;
    EventLoopGroup group;
    private BundleContext bundleContext;

    public AgentdThreadFactoryImpl(BundleContext context) {
        this.bundleContext = context;
        this.delegate = new DefaultThreadFactory("agentd-thread-factory");
        String initThreadsString = context.getProperty("initial.threads");
        int threads = 8;
        if (initThreadsString != null) {
            try {
                threads = Integer.parseInt(initThreadsString);
            } catch (Exception ex) {

            }
        } else {
        }
        this.group = new NioEventLoopGroup(8, this.delegate);
    }

    @Override
    public ThreadFactory getThreadFactory() {
        return this.delegate;
    }

    @Override
    public EventLoopGroup getEventLoopGroup() {
        return this.group;
    }
}
