 package com.wincom.dcim.agentd.internal;

import java.util.Dictionary;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.internal.tests.AcceptState;
import com.wincom.dcim.agentd.internal.tests.ReceiveState;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Accept;
import com.wincom.dcim.agentd.statemachine.*;
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

    private static void testServerChannelFactory(BundleContext bundleContext) {
        ServiceReference<NetworkService> serviceRef = bundleContext.getServiceReference(NetworkService.class);
        NetworkService service = bundleContext.getService(serviceRef);
        out.println(serviceRef);
        out.println(service);

        createAcceptor(service);
        for (int i = 0; i < 10000; ++i) {
            createConnection(service);
        }
    }

    private static void createAcceptor(NetworkService service) {
        HandlerContext handlerContext = service.createHandlerContext();

        handlerContext.send(new Accept(handlerContext, "0.0.0.0", 9080));
    }

    static void createConnection(NetworkService service) {
        HandlerContext handlerContext = service.createHandlerContext();
        StateMachineBuilder builder = new StateMachineBuilder();

        StateMachine client = builder
                .add("connectState", new ConnectState(handlerContext, "192.168.0.68", 9080))
                .add("receiveState", new ReceiveState())
                .add("waitState", new WaitTimeoutState(6000))
                .transision("connectState", "receiveState", "waitState", "waitState")
                .transision("receiveState", "receiveState", "waitState", "waitState")
                .transision("waitState", "connectState", "connectState", "waitState")
                .buildWithInitialState("connectState");
    }

    public static void main(String[] args) {
        NetworkService service = new NetworkServiceImpl();
        for (int i = 0; i < 1; ++i) {
            createConnection(service);
        }
    }
}
