package com.wincom.dcim.integration.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.NetworkService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public final class AggregationActivator
        implements BundleActivator {

    @Override
    public void start(BundleContext bc)
            throws Exception {
        ServiceReference<AgentdService> agentRef = bc.getServiceReference(AgentdService.class);
        AgentdService agent = bc.getService(agentRef);

        ServiceReference<NetworkService> networkRef = bc.getServiceReference(NetworkService.class);
        NetworkService network = bc.getService(networkRef);
        TestStarter starter = new TestStarter(agent, network);
        bc.addServiceListener(starter);
    }

    @Override
    public void stop(BundleContext bc)
            throws Exception {
    }
}
