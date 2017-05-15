package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.internal.tests.ConnectState;
import com.wincom.dcim.agentd.internal.tests.ReceiveState;
import com.wincom.dcim.agentd.internal.tests.WaitTimeoutState;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.statemachine.StateMachine;
import com.wincom.dcim.agentd.statemachine.StateMachineBuilder;
import static java.lang.System.out;
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
    public HandlerContext createOutbound(AgentdService service, Properties inbound) {
        out.println(inbound);
        log.info(inbound.toString());

        final HandlerContext handlerContext = network.createHandlerContext();
        final StateMachineBuilder builder = new StateMachineBuilder();
        final String host = inbound.getProperty(HOST_KEY);
        final String port = inbound.getProperty(PORT_KEY);
        final String waiteTimeout = inbound.getProperty(WAITE_TIMEOUT_KEY);

        StateMachine client = builder
                .add("connectState", new ConnectState(handlerContext, host, Integer.valueOf(port)))
                .add("receiveState", new ReceiveState(handlerContext))
                .add("waitState", new WaitTimeoutState(handlerContext, Integer.valueOf(waiteTimeout)))
                .transision("connectState", "receiveState", "waitState")
                .transision("receiveState", "receiveState", "waitState")
                .transision("waitState", "connectState", "connectState")
                .buildWithInitialState("connectState");

        handlerContext.getStateMachine()
                .buildWith(client)
                .enter(handlerContext);

        return handlerContext;
    }
}
