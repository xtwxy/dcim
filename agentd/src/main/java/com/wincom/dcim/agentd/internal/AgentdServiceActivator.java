package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.Acceptor;
import java.util.Dictionary;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.AgentdThreadFactory;
import com.wincom.dcim.agentd.ClientChannelFactory;
import com.wincom.dcim.agentd.ServerChannelFactory;
import io.netty.channel.Channel;
import static java.lang.System.out;
import java.util.Properties;
import org.osgi.framework.ServiceReference;

public final class AgentdServiceActivator implements BundleActivator {

    @Override
    public void start(BundleContext bc) throws Exception {
        Dictionary props = new Properties();

        bc.addServiceListener(new ServiceListenerImpl());

        bc.registerService(AgentdService.class, new AgentdServiceImpl(bc), props);
        bc.registerService(AgentdThreadFactory.class, new AgentdThreadFactoryImpl(bc), props);
        bc.registerService(ClientChannelFactory.class, new ClientChannelFactoryImpl(bc), props);
        bc.registerService(ServerChannelFactory.class, new ServerChannelFactoryImpl(bc), props);

        testServerChannelFactory(bc);
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
  
    }
    
    private void testServerChannelFactory(BundleContext bundleContext) {
        ServiceReference<ServerChannelFactory> serverRef = bundleContext.getServiceReference(ServerChannelFactory.class);
        ServerChannelFactory server = bundleContext.getService(serverRef);
        out.println(serverRef);
        out.println(server);
        server.create("wangxy", 9080, new Acceptor() {
            @Override
            public void onAccept(Channel ch) {
                out.println(ch);
            }
            
        });
    }
}
