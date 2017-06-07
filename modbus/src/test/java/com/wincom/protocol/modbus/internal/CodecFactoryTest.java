package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.protocol.modbus.internal.master.MasterCodecFactoryImpl;
import com.wincom.protocol.modbus.internal.master.MasterCodecImpl;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.internal.AgentdServiceImpl;
import com.wincom.dcim.agentd.internal.NetworkServiceImpl;
import com.wincom.dcim.agentd.internal.TcpClientCodecImpl;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.NetworkConfig;
import com.wincom.dcim.agentd.primitives.ApplicationFailure;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.protocol.modbus.ReadMultipleHoldingRegistersRequest;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class CodecFactoryTest {

    Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String MODBUS_FACTORY_ID = "1000";
    private static final String TCP_CODEC_ID = "1000";
    private static final String MODBUS_CODEC_ID = "1001";
    private static final String MODBUS_ADDRESS_1 = "1";
    private static final String MODBUS_ADDRESS_2 = "2";
    private static final String HOST = "localhost";
    private static final String PORT = "9080";
    private static final String WAITE_TIMEOUT = "60000";

    HandlerContext outboundContext;

    public void test() {
        AgentdServiceImpl agent = new AgentdServiceImpl();
        NetworkServiceImpl network = new NetworkServiceImpl();

        TcpClientCodecImpl tcpCodec = new TcpClientCodecImpl(network);
        agent.setCodec(TCP_CODEC_ID, tcpCodec);

        MasterCodecFactoryImpl modbusFactory = new MasterCodecFactoryImpl();

        agent.registerCodecFactory(MODBUS_FACTORY_ID, modbusFactory);

        Properties props = new Properties();

        props.put(MasterCodecFactoryImpl.OUTBOUND_CODEC_ID_KEY, TCP_CODEC_ID);

        Properties tcpOutbound = new Properties();
        tcpOutbound.put(NetworkConfig.HOST_KEY, HOST);
        tcpOutbound.put(NetworkConfig.PORT_KEY, PORT);
        tcpOutbound.put(NetworkConfig.WAITE_TIMEOUT_KEY, WAITE_TIMEOUT);

        props.put(MasterCodecFactoryImpl.OUTBOUND_CTX_PROPS_KEY, tcpOutbound);

        try {
            Codec c = agent.createCodec(MODBUS_FACTORY_ID, MODBUS_CODEC_ID, props);

            Properties modbusOutbound = new Properties();
            modbusOutbound.put(MasterCodecImpl.ADDRESS_KEY, MODBUS_ADDRESS_1);

            HandlerContext inboundHandlerContext = new HandlerContext.Adapter() {
                public void fire(Message m) {
                    m.apply(this, new ChannelInboundHandler.Adapter() {
                        @Override
                        public void handleChannelActive(HandlerContext ctx, ChannelActive m) {
                            log.info(String.format("handleChannelActive(%s, %s)", ctx, m));
                            ctx.setActive(true);
                            sendRequest();
                        }

                        @Override
                        public void handlePayloadReceived(HandlerContext ctx, Message m) {
                            log.info(String.format("handlePayloadReceived(%s, %s)", ctx, m));
                        }

                        @Override
                        public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m) {
                            log.info(String.format("handleChannelTimeout(%s, %s)", ctx, m));
                            super.handleChannelTimeout(ctx, m);
                            sendRequest();
                        }

                        @Override
                        public void handleApplicationFailure(HandlerContext ctx, ApplicationFailure m) {
                            ctx.onRequestCompleted(m);
                            sendRequest();
                        }

                    });
                }
            };
            outboundContext = c.openInbound(agent, modbusOutbound);
            outboundContext.addInboundContext(inboundHandlerContext);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void sendRequest() {
        ReadMultipleHoldingRegistersRequest request = new ReadMultipleHoldingRegistersRequest();
        request.setStartAddress((short) 0x01f4);
        request.setNumberOfRegisters((short) 10);

        outboundContext.send(request);
    }

    public static void main(String[] args) {
        CodecFactoryTest t = new CodecFactoryTest();
        t.test();
    }
}
