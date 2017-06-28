package com.wincom.dcim.integration.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.NetworkConfig;
import com.wincom.dcim.agentd.NetworkService;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class TestStarter implements ServiceListener, Runnable {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String OUTBOUND_CODEC_ID_KEY = "codecId";
    private static final String OUTBOUND_CTX_PROPS_KEY = "outboundProps";
    private static final String TCP_CODEC_ID = "TCP_CODEC";

    private static final String MP3000_FACTORY_ID = "MP3000";
    private static final String MODBUS_SLAVE_FACTORY_ID = "MODBUS_SLAVE";
    private static final String DDS3366D_FACTORY_ID = "DDS3366D";
    
    private static final String MODBUS_SLAVE_ADDRESS_KEY = "ADDRESS";

    private static final String MP3000_BASE_PORT_KEY = "basePort";
    private static final String MP3000_PORT_COUNT_KEY = "portCount";
    private static final String MP3000_HOST = "localhost";
    private static final String MP3000_BASE_PORT = "9080";
    private static final String COM_PORT_KEY = "port";

    private static final int MP3000_COUNT = 1;
    private static final int MP3000_PORT_COUNT = 1;
    private static final int MODBUS_CODEC_COUNT = MP3000_COUNT * MP3000_PORT_COUNT;
    private static final int DDS3366D_COUNT_PER_PORT = 1;
    private static final int DDS3366D_COUNT = MODBUS_CODEC_COUNT * DDS3366D_COUNT_PER_PORT;
    private static final String MP3000_WAITE_TIMEOUT = "60000";

    private final Set<String> required;
    private final AgentdService agent;
    private final NetworkService network;
    private boolean started;

    TestStarter(AgentdService agent, NetworkService network) {
        this.required = new HashSet<>();
        this.agent = agent;
        this.network = network;
        this.started = false;

        required.add("MP3000");
        required.add("MODBUS_SLAVE");
        required.add("DDS3366D");
        required.removeAll(agent.getCodecFactoryKeys());
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        required.removeAll(agent.getCodecFactoryKeys());
        tryStart();
    }

    public synchronized void tryStart() {
        if (started || !required.isEmpty()) {
            return;
        }
        Thread t = new Thread(this);
        t.start();
        started = true;
    }

    private void createMp3000Codec(AgentdService agent, NetworkService network) {
        for (int i = 0; i < MP3000_COUNT; ++i) {
            Properties props = new Properties();
            props.setProperty(NetworkConfig.HOST_KEY, MP3000_HOST);
            props.setProperty(NetworkConfig.WAITE_TIMEOUT_KEY, MP3000_WAITE_TIMEOUT);
            props.setProperty(MP3000_BASE_PORT_KEY, MP3000_BASE_PORT);
            props.setProperty(MP3000_PORT_COUNT_KEY, Integer.toString(MP3000_PORT_COUNT));
            props.setProperty(OUTBOUND_CODEC_ID_KEY, TCP_CODEC_ID);

            agent.createCodec(MP3000_FACTORY_ID, Integer.toString(i + 1), props);
        }
    }

    private void createModbusCodec(AgentdService agent, NetworkService network) {
        for (int i = 0; i < MP3000_COUNT; ++i) {
            for (int j = 0; j < MP3000_PORT_COUNT; ++j) {
                Properties props = new Properties();
                props.setProperty(OUTBOUND_CODEC_ID_KEY, Integer.toString(i + 1));
                Properties outbound = new Properties();
                outbound.setProperty(COM_PORT_KEY, Integer.toString(j));
                props.put(OUTBOUND_CTX_PROPS_KEY, outbound);
                
                props.setProperty(COM_PORT_KEY, Integer.toString(i + 1));

                agent.createCodec(MODBUS_SLAVE_FACTORY_ID, Integer.toString(MP3000_COUNT + (i * MP3000_PORT_COUNT) + j + 1), props);
            }
        }
    }

    private void createDds3366dCodec(AgentdService agent, NetworkService network) {
        for (int i = 0; i < MODBUS_CODEC_COUNT; ++i) {
            for (int j = 0; j < DDS3366D_COUNT_PER_PORT; ++j) {
                Properties props = new Properties();
                props.setProperty(OUTBOUND_CODEC_ID_KEY, Integer.toString(MP3000_COUNT + i + 1));
                
                Properties outbound = new Properties();
                outbound.setProperty(MODBUS_SLAVE_ADDRESS_KEY, Integer.toString(j + 1));
                props.put(OUTBOUND_CTX_PROPS_KEY, outbound);

                agent.createCodec(DDS3366D_FACTORY_ID, Integer.toString(MP3000_COUNT + MODBUS_CODEC_COUNT + i + 1), props);
            }
        }
    }

    private void createReader(AgentdService agent, NetworkService network) {
        for(int i = 0; i < DDS3366D_COUNT; ++i) {
            TestHandlerContextImpl ctx = new TestHandlerContextImpl();
            Codec codec = agent.getCodec(Integer.toString(MP3000_COUNT + MODBUS_CODEC_COUNT + i + 1));
            Properties props = new Properties();
            HandlerContext outbound = codec.openInbound(props);
            log.info(outbound.toString());
            outbound.addInboundContext(ctx);
        }
    }

    @Override
    public void run() {
        log.info(agent.toString());
        createMp3000Codec(agent, network);
        log.info(agent.toString());
        createModbusCodec(agent, network);
        log.info(agent.toString());
        createDds3366dCodec(agent, network);
        log.info(agent.toString());
        createReader(agent, network);
        log.info(agent.toString());
    }
}
