package com.wincom.dcim.agentd.internal;

import java.util.Dictionary;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.internal.tests.AcceptState;
import com.wincom.dcim.agentd.internal.tests.ConnectState;
import com.wincom.dcim.agentd.internal.tests.ReceiveState;
import com.wincom.dcim.agentd.primitives.Accept;
import com.wincom.dcim.agentd.primitives.Connect;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.dcim.agentd.statemachine.StateBuilder;
import static java.lang.System.out;
import java.util.Properties;
import org.osgi.framework.ServiceReference;

public final class AgentdServiceActivator implements BundleActivator {

    @Override
    public void start(BundleContext bc) throws Exception {
        Dictionary props = new Properties();

        bc.addServiceListener(new ServiceListenerImpl());

        bc.registerService(AgentdService.class, new AgentdServiceImpl(bc), props);
        bc.registerService(NetworkService.class, new NetworkServiceImpl(bc), props);
        testServerChannelFactory(bc);
    }

    @Override
    public void stop(BundleContext bc) throws Exception {

    }

    private void testServerChannelFactory(BundleContext bundleContext) {
        ServiceReference<NetworkService> serviceRef = bundleContext.getServiceReference(NetworkService.class);
        NetworkService service = bundleContext.getService(serviceRef);
        out.println(serviceRef);
        out.println(service);

        //createAcceptor(service);
        for(int i = 0; i < 2000; ++i) {
            createConnection(service);
        }
    }

    private void createAcceptor(NetworkService service) {
        HandlerContext handlerContext = service.createHandlerContext();
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

        handlerContext.send(new Accept("0.0.0.0", 9080));
    }

    private void createConnection(NetworkService service) {
        HandlerContext handlerContext = service.createHandlerContext();
        StateBuilder client = StateBuilder
                .initial().state(new ConnectState(service, handlerContext));
        
        client.success().state(new ReceiveState());

        client.fail().state(new State.Adapter() {
            @Override
            public State on(HandlerContext ctx, Message m) {
                out.println("Create server failed.");
                return success();
            }
        });

        handlerContext.getStateMachine()
                .buildWith(client)
                .enter();

        handlerContext.send(new Connect("192.168.0.68", 9080));
    }

}
