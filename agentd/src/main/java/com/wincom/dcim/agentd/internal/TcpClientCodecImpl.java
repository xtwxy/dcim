package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.primitives.ChannelInboundHandler;
import com.wincom.dcim.agentd.primitives.ChannelOutboundHandler;
import com.wincom.dcim.agentd.statemachine.ConnectState;
import com.wincom.dcim.agentd.statemachine.ReceiveState;
import com.wincom.dcim.agentd.statemachine.WaitTimeoutState;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import com.wincom.dcim.agentd.statemachine.StateMachineBuilder;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class TcpClientCodecImpl implements Codec {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final String PORT_KEY = "port";
    public static final String HOST_KEY = "host";
    public static final String WAITE_TIMEOUT_KEY = "waiteTimeout";

    private final NetworkService network;

    public TcpClientCodecImpl(NetworkService network) {
        this.network = network;
    }

    @Override
    public ChannelOutboundHandler openOutbound(AgentdService service, Properties outbound, ChannelInboundHandler inboundHandler) {
        log.info(outbound.toString());

        final StreamHandlerContextImpl handlerContext = (StreamHandlerContextImpl)network.createHandlerContext();
        final StateMachineBuilder builder = new StateMachineBuilder();
        final String host = outbound.getProperty(HOST_KEY);
        final String port = outbound.getProperty(PORT_KEY);
        final String waiteTimeout = outbound.getProperty(WAITE_TIMEOUT_KEY);

        StateMachine client = builder
                .add("connectState", new ConnectState(handlerContext, host, Integer.valueOf(port)))
                .add("receiveState", new ReceiveState())
                .add("waitState", new WaitTimeoutState(Integer.valueOf(waiteTimeout)))
                .transision("connectState", "receiveState", "waitState")
                .transision("receiveState", "receiveState", "waitState")
                .transision("waitState", "connectState", "connectState")
                .buildWithInitialState("connectState");
        
        handlerContext.setInboundHandler(inboundHandler);
        handlerContext.getStateMachine()
                .buildWith(client)
                .enter(handlerContext);

        return handlerContext.getOutboundHandler();
    }
}
