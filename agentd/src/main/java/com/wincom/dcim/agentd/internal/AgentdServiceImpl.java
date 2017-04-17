package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.AgentdService;
import org.osgi.framework.BundleContext;

public final class AgentdServiceImpl implements AgentdService {

    private BundleContext bundleContext;

    public AgentdServiceImpl(BundleContext context) {
        this.bundleContext = context;
    }

}
