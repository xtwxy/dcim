package com.wincom.dcim.agentd.internal;

import java.util.Dictionary;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.internal.tests.AcceptState;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.dcim.agentd.statemachine.StateBuilder;
import com.wincom.dcim.agentd.statemachine.nettyimpl.HandlerContextImpl;
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

        final HandlerContext handlerContext = new HandlerContextImpl(service.getEventLoopGroup());

        StateBuilder server = StateBuilder
                .initial().state(new AcceptState(service, handlerContext));

        server.fail().state(new State.Adapter() {
            @Override
            public State on(HandlerContext ctx, Message m) {
                out.println("Create server failed.");
                return success();
            }
        });

        handlerContext.getStateMachine()
                .buildWith(server)
                .enter();
    }
}
