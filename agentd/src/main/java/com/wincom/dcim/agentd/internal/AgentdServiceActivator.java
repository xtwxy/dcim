package com.wincom.dcim.agentd.internal;

import java.util.Dictionary;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.primitives.Accept;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import static java.lang.System.out;
import java.util.Properties;
import org.osgi.framework.ServiceReference;

public final class AgentdServiceActivator implements BundleActivator {

    @Override
    public void start(BundleContext bc) throws Exception {
        Dictionary props = new Properties();

        bc.addServiceListener(new ServiceListenerImpl());

        bc.registerService(AgentdService.class, new AgentdServiceImpl(bc), props);
        testServerChannelFactory(bc);
    }

    @Override
    public void stop(BundleContext bc) throws Exception {

    }

    private void testServerChannelFactory(BundleContext bundleContext) {
        ServiceReference<AgentdService> serviceRef = bundleContext.getServiceReference(AgentdService.class);
        AgentdService service = bundleContext.getService(serviceRef);
        out.println(serviceRef);
        out.println(service);

        final HandlerContext handlerContext = service.createHandlerContext();
        
        handlerContext.send(new Accept("0.0.0.0", 9080));
    }
}
