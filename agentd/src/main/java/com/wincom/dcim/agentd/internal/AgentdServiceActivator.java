package com.wincom.dcim.agentd.internal;

import java.util.Dictionary;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Accept;
import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SendBytes;
import com.wincom.dcim.agentd.statemachine.*;
import static java.lang.System.out;
import java.nio.ByteBuffer;
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
        HandlerContext handlerContext = service.createStreamHandlerContext();

        handlerContext.send(new Accept(handlerContext, "0.0.0.0", 9080));
    }

    static void createConnection(NetworkService service) {
        HandlerContext handlerContext = service.createStreamHandlerContext();

        StateMachineBuilder builder = new StateMachineBuilder();

        StateMachine client = builder
                .add("connectState", new ConnectState(handlerContext, "localhost", 9080))
                .add("receiveState", new ReceiveState())
                .add("waitState", new WaitTimeoutState(6000))
                .transision("connectState", "receiveState", "waitState", "waitState")
                .transision("receiveState", "receiveState", "waitState", "waitState")
                .transision("waitState", "connectState", "connectState", "waitState")
                .buildWithInitialState("connectState");
        handlerContext.addInboundContext(new HandlerContext.Adapter() {
            @Override
            public void fire(Message m) {
                m.apply(this, new ChannelInboundHandler.Adapter() {
                    @Override
                    public void handleChannelActive(HandlerContext ctx, ChannelActive m) {
                        ctx.setActive(true);
                        handlerContext.send(new SendBytes(ctx, ByteBuffer.wrap("Hello, World!".getBytes())));
                        log.info(String.format("handleChannelActive(%s, %s)", ctx, m));
                    }

                    @Override
                    public void handlePayloadReceived(HandlerContext ctx, Message m) {
                        log.info(String.format("handlePayloadReceived(%s, %s)", ctx, m));
                        if(m instanceof BytesReceived) {
                            BytesReceived b = (BytesReceived) m;
                            handlerContext.send(new SendBytes(handlerContext, b.getByteBuffer()));
                        }
                    }

                    @Override
                    public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m) {
                        super.handleChannelTimeout(ctx, m);
                        log.info(String.format("handleChannelTimeout(%s, %s)", ctx, m));
                    }
                });
            }
        });
        handlerContext.state(client);
        client.enter(handlerContext);
    }

    public static void main(String[] args) {
        NetworkService service = new NetworkServiceImpl();
        for (int i = 0; i < 1; ++i) {
            createConnection(service);
        }
    }
}
