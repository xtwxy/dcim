package com.wincom.dcim.agentd.internal;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.Accept;
import com.wincom.dcim.agentd.messages.BytesReceived;
import com.wincom.dcim.agentd.messages.ChannelActive;
import com.wincom.dcim.agentd.messages.ChannelTimeout;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.messages.SendBytes;
import com.wincom.dcim.agentd.statemachine.*;
import static java.lang.System.out;
import java.nio.ByteBuffer;
import java.util.Hashtable;

import org.osgi.framework.ServiceReference;

public final class AgentdServiceActivator implements BundleActivator {

    private static final String TCP_CODEC_ID = "TCP_CODEC";
    
    @Override
    public void start(BundleContext bc) throws Exception {
        Hashtable<String, ?> props = new Hashtable<>();
        
        bc.addServiceListener(new ServiceListenerImpl());
        
        AgentdService agent = new AgentdServiceImpl(bc);
        NetworkService network = new NetworkServiceImpl(bc);
        
        bc.registerService(AgentdService.class, agent, props);
        bc.registerService(NetworkService.class, new NetworkServiceImpl(bc), props);

        createTcpCodec(agent, network);
        //testServerChannelFactory(bc);
    }

    @Override
    public void stop(BundleContext bc) throws Exception {

    }
    
    private static void createTcpCodec(AgentdService agent, NetworkService network) {
        TcpClientCodecImpl tcpCodec = new TcpClientCodecImpl(network);
        agent.setCodec(TCP_CODEC_ID, tcpCodec);
    }

    private static void testServerChannelFactory(BundleContext bundleContext) {
        ServiceReference<NetworkService> serviceRef = bundleContext.getServiceReference(NetworkService.class);
        NetworkService service = bundleContext.getService(serviceRef);
        out.println(serviceRef);
        out.println(service);

        createAcceptor(service);
        for (int i = 0; i < 1; ++i) {
            createConnection(service);
        }
    }

    private static void createAcceptor(NetworkService service) {
        HandlerContext handlerContext = service.createStreamHandlerContext();

        handlerContext.send(new Accept(handlerContext, "0.0.0.0", 9080));
    }

    private static void createConnection(NetworkService service) {
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
