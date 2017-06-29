package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.NetworkConfig;
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

    private final NetworkService network;

    public TcpClientCodecImpl(NetworkService network) {
        this.network = network;
    }

    @Override
    public HandlerContext openInbound(Properties outbound) {
        log.debug(outbound.toString());

        final StreamHandlerContextImpl handlerContext = (StreamHandlerContextImpl) network.createStreamHandlerContext();
        final StateMachineBuilder builder = new StateMachineBuilder();
        final String host = outbound.getProperty(NetworkConfig.HOST_KEY);
        log.debug(host);
        final String port = outbound.getProperty(NetworkConfig.PORT_KEY);
        log.debug(port);
        final String waiteTimeout = outbound.getProperty(NetworkConfig.WAITE_TIMEOUT_KEY);

        StateMachine client = builder
                .add("connectState", new ConnectState(handlerContext, host, Integer.valueOf(port)))
                .add("receiveState", new ReceiveState())
                .add("waitState", new WaitTimeoutState(Integer.valueOf(waiteTimeout)))
                .transision("connectState", "receiveState", "waitState", "waitState")
                .transision("receiveState", "receiveState", "waitState", "waitState")
                .transision("waitState", "connectState", "connectState", "waitState")
                .buildWithInitialState("connectState");

        handlerContext.state(client);
        client.enter(handlerContext);

        return handlerContext;
    }
}
