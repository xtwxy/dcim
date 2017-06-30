package com.wincom.driver.dds3366d.internal.tests;

import java.util.Properties;
import java.util.Set;

import com.wincom.driver.dds3366d.internal.DDS3366DCodecFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.NetworkConfig;
import com.wincom.dcim.agentd.internal.AgentdServiceImpl;
import com.wincom.dcim.agentd.internal.NetworkServiceImpl;
import com.wincom.dcim.agentd.internal.TcpClientCodecImpl;
import com.wincom.dcim.agentd.messages.ApplicationFailure;
import com.wincom.dcim.agentd.messages.ChannelActive;
import com.wincom.dcim.agentd.messages.ChannelTimeout;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.primitives.GetSignalValues;
import com.wincom.protocol.modbus.internal.master.MasterCodecFactoryImpl;
import com.wincom.protocol.modbus.internal.master.MasterCodecImpl;

/**
 *
 * @author master
 */
public class CodecFactoryTest {

    Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String DDS3366D_FACTORY_ID = "DDS3366D";
    private static final String MODBUS_FACTORY_ID = "MODBUS_SLAVE";
    private static final String TCP_CODEC_ID = "1000";
    private static final String MODBUS_CODEC_ID = "1001";
    private static final String DDS3366D_CODEC_ID = "1002";
    private static final String MODBUS_ADDRESS_1 = "1";
    private static final String HOST = "localhost";
    private static final String PORT = "9080";
    private static final String WAITE_TIMEOUT = "60000";

    HandlerContext outboundContext;

    public void test() {
        AgentdServiceImpl agent = new AgentdServiceImpl();
        NetworkServiceImpl network = new NetworkServiceImpl();

        registerCodecsAndFactories(network, agent);

        createModbusCodec(agent);
        createDDS3366DCodec(agent);
    }

    private void registerCodecsAndFactories(NetworkServiceImpl network, AgentdServiceImpl agent) {
        TcpClientCodecImpl tcpCodec = new TcpClientCodecImpl(network);
        agent.setCodec(TCP_CODEC_ID, tcpCodec);

        MasterCodecFactoryImpl modbusFactory = new MasterCodecFactoryImpl(agent);
        agent.registerCodecFactory(MODBUS_FACTORY_ID, modbusFactory);
        DDS3366DCodecFactoryImpl dds3366dFactory = new DDS3366DCodecFactoryImpl(agent);
        agent.registerCodecFactory(DDS3366D_FACTORY_ID, dds3366dFactory);
    }

    private void createModbusCodec(AgentdServiceImpl agent) {
        Properties props = new Properties();

        props.put(MasterCodecFactoryImpl.OUTBOUND_CODEC_ID_KEY, TCP_CODEC_ID);

        Properties tcpOutbound = new Properties();
        tcpOutbound.put(NetworkConfig.HOST_KEY, HOST);
        tcpOutbound.put(NetworkConfig.PORT_KEY, PORT);
        tcpOutbound.put(NetworkConfig.WAITE_TIMEOUT_KEY, WAITE_TIMEOUT);

        props.put(MasterCodecFactoryImpl.OUTBOUND_CTX_PROPS_KEY, tcpOutbound);

        agent.createCodec(MODBUS_FACTORY_ID, MODBUS_CODEC_ID, props);
    }

    private void createDDS3366DCodec(AgentdServiceImpl agent) {
        Properties props = new Properties();
        Properties modbusOutbound = new Properties();
        modbusOutbound.put(MasterCodecImpl.MODBUS_SLAVE_ADDRESS_KEY, MODBUS_ADDRESS_1);
        props.put(DDS3366DCodecFactoryImpl.OUTBOUND_CTX_PROPS_KEY, modbusOutbound);
        props.put(DDS3366DCodecFactoryImpl.OUTBOUND_CODEC_ID_KEY, MODBUS_CODEC_ID);

        try {
            Codec dds3366dCodec = agent.createCodec(DDS3366D_FACTORY_ID, DDS3366D_CODEC_ID, props);

            HandlerContext inboundHandlerContext = new HandlerContext.Adapter() {
                @Override
                public void fire(Message m) {
                    m.apply(this, new ChannelInboundHandler.Adapter() {
                        @Override
                        public void handleChannelActive(HandlerContext ctx, ChannelActive m) {
                            log.info(String.format("handleChannelActive(%s, %s)", ctx, m));
                            ctx.setActive(true);
                            sendRequest(ctx);
                        }

                        @Override
                        public void handlePayloadReceived(HandlerContext ctx, Message m) {
                            log.info(m.toString());
                            sendRequest(ctx);
                        }

                        @Override
                        public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m) {
                            log.info(String.format("handleChannelTimeout(%s, %s)", ctx, m));
                            super.handleChannelTimeout(ctx, m);
                            sendRequest(ctx);
                        }

                        @Override
                        public void handleApplicationFailure(HandlerContext ctx, ApplicationFailure m) {
                            ctx.onRequestCompleted();
                            sendRequest(ctx);
                        }

                    });
                }
            };
            outboundContext = dds3366dCodec.openInbound(modbusOutbound);
            outboundContext.addInboundContext(inboundHandlerContext);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void sendRequest(HandlerContext ctx) {
        GetSignalValues.Request request = new GetSignalValues.Request(ctx);
        Set<String> keys = request.getKeys();
        keys.add("activePowerCombo");
        keys.add("positiveActivePower");
        keys.add("reverseActivePower");
        keys.add("voltage");
        keys.add("current");
        keys.add("power");
        keys.add("powerFactor");
        keys.add("frequency");
        keys.add("clock");
        keys.add("slaveAddress");
        keys.add("pt");
        keys.add("ct");
        
        outboundContext.send(request);
    }

    public static void main(String[] args) {
        for(int i = 0; i < 4; ++i) {
            CodecFactoryTest t = new CodecFactoryTest();
            t.test();
        }
    }
}
