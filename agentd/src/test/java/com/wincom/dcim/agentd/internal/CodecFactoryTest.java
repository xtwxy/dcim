package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.internal.mocks.CodecFactoryImpl;
import com.wincom.dcim.agentd.internal.mocks.InboundHandlerImpl;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SendBytes;
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
    private static final String FACTORY_ID = "1000";
    private static final String TCP_CODEC_ID = "1000";
    private static final String CODEC_ID = "1001";
    private static final String HOST = "localhost";
    private static final String PORT = "9080";
    private static final String WAITE_TIMEOUT = "60000";

    public void test() {
        AgentdServiceImpl agent = new AgentdServiceImpl();
        NetworkServiceImpl network = new NetworkServiceImpl();
        CodecFactoryImpl factory = new CodecFactoryImpl();
        TcpClientCodecImpl tcpCodec = new TcpClientCodecImpl(network);
        agent.setCodec(TCP_CODEC_ID, tcpCodec);

        agent.registerCodecFactory(FACTORY_ID, factory);

        Properties props = new Properties();

        props.put(CodecFactoryImpl.OUTBOUND_CODEC_ID_KEY, TCP_CODEC_ID);

        Properties outbound = new Properties();
        outbound.put(TcpClientCodecImpl.HOST_KEY, HOST);
        outbound.put(TcpClientCodecImpl.PORT_KEY, PORT);
        outbound.put(TcpClientCodecImpl.WAITE_TIMEOUT_KEY, WAITE_TIMEOUT);

        props.put(CodecFactoryImpl.OUTBOUND_CTX_PROPS_KEY, outbound);

        try {
            Codec c = agent.createCodec(FACTORY_ID, CODEC_ID, props);
            HandlerContext inboundHandlerContext = new HandlerContext.NullContext() {
                public void fire(Message m) {
                    log.info(String.format("fire(%s)", m));
                }
            };
            c.openInbound(agent, outbound, inboundHandlerContext);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    private static final byte[] BA = new byte[128];

    static {
        for (int i = 0; i < BA.length; ++i) {
            BA[i] = (byte) (0xff & (i % 10 + '0'));
        }
    }

    private static void sendBytes(HandlerContext ctx) {
        ByteBuffer buffer = ByteBuffer.wrap(BA);
        ctx.send(new SendBytes(ctx, buffer));
    }

    public static void main(String[] args) {
        CodecFactoryTest t = new CodecFactoryTest();
        t.test();
    }
}
