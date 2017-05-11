package com.wincom.dcim.agentd.internal;

import java.util.Dictionary;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.internal.tests.AcceptState;
import com.wincom.dcim.agentd.internal.tests.ConnectState;
import com.wincom.dcim.agentd.internal.tests.ReceiveState;
import com.wincom.dcim.agentd.internal.tests.WaitTimeoutState;
import com.wincom.dcim.agentd.primitives.Accept;
import com.wincom.dcim.agentd.primitives.Connect;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;
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

        //createAcceptor(service);
        for (int i = 0; i < 20000; ++i) {
            createConnection(service);
        }
    }

    private static void createAcceptor(NetworkService service) {
        HandlerContext handlerContext = service.createHandlerContext();

        StateMachineBuilder builder = new StateMachineBuilder();

        StateMachine server = builder
                .add("acceptState", new AcceptState(service, handlerContext))
                .add("failState", new State.Adapter() {
                    @Override
                    public State on(HandlerContext ctx, Message m) {
                        out.println("Create server failed.");
                        return success();
                    }
                })
                .transision("acceptState", "acceptState", "failState")
                .transision("failState", "failState", "failState")
                .buildWithInitialAndStop("acceptState", "failState");

        handlerContext.getStateMachine()
                .buildWith(server)
                .enter();

        handlerContext.send(new Accept("0.0.0.0", 9080));
    }

    static void createConnection(NetworkService service) {
        HandlerContext handlerContext = service.createHandlerContext();

        StateMachineBuilder builder = new StateMachineBuilder();

        StateMachine client = builder
                .add("connectState", new ConnectState(service, handlerContext) {
                    @Override
                    public State enter() {
                        this.handlerContext.send(new Connect("192.168.0.68", 9080));
                        return this;
                    }
                    @Override
                    public State fail() {
                        this.handlerContext.send(new Connect("192.168.0.68", 9080));
                        return this;
                    }
                })
                .add("receiveState", new ReceiveState())
                .add("waitState", new WaitTimeoutState(handlerContext, 60))
                .transision("connectState", "receiveState", "waitState")
                .transision("receiveState", "receiveState", "waitState")
                .transision("waitState", "connectState", "connectState")
                .buildWithInitialState("connectState");

        handlerContext.getStateMachine()
                .buildWith(client)
                .enter();

    }

    public static void main(String[] args) {
        NetworkService service = new NetworkServiceImpl(null);
        for (int i = 0; i < 40000; ++i) {
            createConnection(service);
        }

    }
}
