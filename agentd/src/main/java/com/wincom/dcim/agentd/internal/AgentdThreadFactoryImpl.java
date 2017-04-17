/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.AgentdThreadFactory;
import io.netty.util.concurrent.DefaultThreadFactory;
import java.util.concurrent.ThreadFactory;
import org.osgi.framework.BundleContext;

/**
 *
 * @author master
 */
public class AgentdThreadFactoryImpl implements AgentdThreadFactory {

    ThreadFactory delegate = new DefaultThreadFactory("agentd-thread-factory");

    private BundleContext bundleContext;

    public AgentdThreadFactoryImpl(BundleContext context) {
        this.bundleContext = context;
    }

    @Override
    public ThreadFactory getThreadFactory() {
        return delegate;
    }
}
