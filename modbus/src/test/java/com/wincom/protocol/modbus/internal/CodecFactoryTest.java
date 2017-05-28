package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.internal.AgentdServiceImpl;
import com.wincom.dcim.agentd.internal.NetworkServiceImpl;
import com.wincom.dcim.agentd.internal.TcpClientCodecImpl;
import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SendBytes;
import com.wincom.dcim.agentd.primitives.WriteComplete;
import com.wincom.dcim.agentd.primitives.WriteTimeout;
import com.wincom.protocol.modbus.ModbusFrame;
import com.wincom.protocol.modbus.ReadMultipleHoldingRegistersRequest;
import java.nio.ByteBuffer;
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

        ModbusCodecFactoryImpl modbusFactory = new ModbusCodecFactoryImpl();

        agent.registerCodecFactory(MODBUS_FACTORY_ID, modbusFactory);

        Properties props = new Properties();

        props.put(ModbusCodecFactoryImpl.OUTBOUND_CODEC_ID_KEY, TCP_CODEC_ID);

        Properties tcpOutbound = new Properties();
        tcpOutbound.put(TcpClientCodecImpl.HOST_KEY, HOST);
        tcpOutbound.put(TcpClientCodecImpl.PORT_KEY, PORT);
        tcpOutbound.put(TcpClientCodecImpl.WAITE_TIMEOUT_KEY, WAITE_TIMEOUT);

        props.put(ModbusCodecFactoryImpl.OUTBOUND_CTX_PROPS_KEY, tcpOutbound);

        try {
            Codec c = agent.createCodec(MODBUS_FACTORY_ID, MODBUS_CODEC_ID, props);

            Properties modbusOutbound = new Properties();
            modbusOutbound.put(ModbusCodecImpl.ADDRESS, MODBUS_ADDRESS_1);

            outboundContext = c.openOutbound(agent, modbusOutbound, new Handler() {
                @Override
                public void handle(HandlerContext ctx, Message m) {
                    log.info(String.format("handle(%s, %s, %s)", this, ctx, m));
                    if (m instanceof BytesReceived) {
                        ctx.send(new SendBytes(((BytesReceived) m).getByteBuffer()));
                    } else if (m instanceof WriteComplete) {
                    } else if (m instanceof WriteTimeout) {
                        sendRequest(outboundContext, ctx);
                    } else if (m instanceof ChannelActive) {
                        sendRequest(outboundContext, ctx);
                    } else {
                        log.info(String.format("Unprocessed: %s", m));
                    }
                }
            });

            final HandlerContext ctx = new HandlerContext.NullContext() {
                @Override
                public void fire(Message m) {
                    log.info("ignored: fire " + m);
                }
            };
            sendRequest(outboundContext, ctx);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static void sendRequest(HandlerContext ctx, HandlerContext reply) {
        ModbusFrame frame = new ModbusFrame();
        ReadMultipleHoldingRegistersRequest request = new ReadMultipleHoldingRegistersRequest();
        request.setStartAddress((short) 0x01f4);
        request.setNumberOfRegisters((short) 10);
        frame.setPayload(request);

        ctx.send(frame, reply);
    }

    public static void main(String[] args) {
        CodecFactoryTest t = new CodecFactoryTest();
        t.test();
    }
}
