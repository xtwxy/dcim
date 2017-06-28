package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.CodecFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public final class DDS3366DActivator
        implements BundleActivator {

    @Override
    public void start(BundleContext bc)
            throws Exception {
        ServiceReference<AgentdService> serviceRef = bc.getServiceReference(AgentdService.class);
        AgentdService agent = bc.getService(serviceRef);
        CodecFactory factory = new DDS3366DCodecFactoryImpl(agent);
        agent.registerCodecFactory("DDS3366D", factory);
    }

    @Override
    public void stop(BundleContext bc)
            throws Exception {
    }
}
