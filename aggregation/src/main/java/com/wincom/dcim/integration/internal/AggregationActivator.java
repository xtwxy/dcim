package com.wincom.dcim.integration.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.NetworkConfig;
import com.wincom.dcim.agentd.NetworkService;
import java.util.Properties;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public final class AggregationActivator
        implements BundleActivator {

    private static final String OUTBOUND_CODEC_ID_KEY = "codecId";
    private static final String TCP_CODEC_ID = "TCP_CODEC";

    private static final String MP3000_FACTORY_ID = "MP3000";
    private static final String MODBUS_SLAVE_FACTORY_ID = "MODBUS_SLAVE";
    private static final String DDS3366D_FACTORY_ID = "DDS3366D";

    private static final String MP3000_BASE_PORT_KEY = "basePort";
    private static final String MP3000_PORT_COUNT_KEY = "portCount";
    private static final String MP3000_HOST = "localhost";
    private static final String MP3000_BASE_PORT = "9080";
    private static final String COM_PORT_KEY = "port";

    private static final int MP3000_COUNT = 1;
    private static final int MP3000_PORT_COUNT = 8;
    private static final int MODBUS_CODEC_COUNT = MP3000_COUNT * MP3000_PORT_COUNT;
    private static final int DDS3366D_COUNT_PER_PORT = 1;
    private static final String MP3000_WAITE_TIMEOUT = "60000";

    @Override
    public void start(BundleContext bc)
            throws Exception {
        ServiceReference<AgentdService> agentRef = bc.getServiceReference(AgentdService.class);
        AgentdService agent = bc.getService(agentRef);

        ServiceReference<NetworkService> networkRef = bc.getServiceReference(NetworkService.class);
        NetworkService network = bc.getService(networkRef);

        createMp3000Codec(agent, network);
        createModbusCodec(agent, network);
        createDds3366dCodec(agent, network);
        createReader(agent, network);
    }

    @Override
    public void stop(BundleContext bc)
            throws Exception {
    }

    private static void createMp3000Codec(AgentdService agent, NetworkService network) {
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

    private static void createModbusCodec(AgentdService agent, NetworkService network) {
        for (int i = 0; i < MP3000_COUNT; ++i) {
            for (int j = 0; j < MP3000_PORT_COUNT; ++j) {
                Properties props = new Properties();
                props.setProperty(OUTBOUND_CODEC_ID_KEY, Integer.toString(i + 1));
                props.setProperty(COM_PORT_KEY, Integer.toString(i + 1));

                agent.createCodec(MODBUS_SLAVE_FACTORY_ID, Integer.toString(MP3000_COUNT + (i * MP3000_PORT_COUNT) + j + 1), props);
            }
        }
    }

    private static void createDds3366dCodec(AgentdService agent, NetworkService network) {
        for (int i = 0; i < MODBUS_CODEC_COUNT; ++i) {
            for (int j = 0; j < DDS3366D_COUNT_PER_PORT; ++j) {
                Properties props = new Properties();
                props.setProperty(OUTBOUND_CODEC_ID_KEY, Integer.toString(MP3000_COUNT + i + 1));

                agent.createCodec(DDS3366D_FACTORY_ID, Integer.toString(MP3000_COUNT + MODBUS_CODEC_COUNT + i + 1), props);
            }
        }
    }

    private static void createReader(AgentdService agent, NetworkService network) {

    }
}
