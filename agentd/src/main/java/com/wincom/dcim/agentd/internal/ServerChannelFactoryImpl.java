package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.Acceptor;
import com.wincom.dcim.agentd.AgentdThreadFactory;
import com.wincom.dcim.agentd.ServerChannelFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import static java.lang.System.out;

public class ServerChannelFactoryImpl implements ServerChannelFactory {

    private BundleContext bundleContext;

    public ServerChannelFactoryImpl(BundleContext context) {
        this.bundleContext = context;
    }

    @Override
    public void create(String host, int port, Acceptor acceptor) {
        out.println("host = " + host
                + ", port = " + port
                + ", acceptor = " + acceptor);

        ServiceReference<AgentdThreadFactory> threadFactoryRef = bundleContext.getServiceReference(AgentdThreadFactory.class);
        AgentdThreadFactory threadFactory = bundleContext.getService(threadFactoryRef);
        out.println(threadFactoryRef);
        out.println(threadFactory);
        
        acceptor.onAccept(null);
    }
}
