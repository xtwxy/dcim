package com.wincom.fsu.mp3000.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.internal.TcpClientCodecImpl;
import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Composition of TCP connections to a MP3000. this CODEC does not handle
 * inbound messages. just pass request to underlying TCP stream.
 *
 * @author master
 */
public class MP3000CodecImpl extends ChannelInboundHandler.Adapter implements Codec {

    private String HOST;
    private final int BASE_PORT;
    private final int PORT_COUNT;
    private final String WAITE_TIMEOUT;
    private final String CODEC_ID;
    public static final String COM_PORT_KEY = "port";
    public static final String OUTBOUND_CODEC_ID_KEY = "codecId";
    public static final String OUTBOUND_CTX_PROPS_KEY = "outboundProps";

    private final Map<Integer, HandlerContext> inbound;

    public MP3000CodecImpl(AgentdService service, Properties props) {
        this.inbound = new HashMap<>();
        this.HOST = props.getProperty("host");
        this.BASE_PORT = Integer.parseInt(props.getProperty("basePort"));
        this.PORT_COUNT = Integer.parseInt(props.getProperty("portCount"));
        this.WAITE_TIMEOUT = props.getProperty(TcpClientCodecImpl.WAITE_TIMEOUT_KEY);
        this.CODEC_ID = props.getProperty(OUTBOUND_CODEC_ID_KEY);
    }

    @Override
    public HandlerContext openOutbound(
            AgentdService service, Properties props, Handler inboundHandler) {
        Integer comport = Integer.valueOf(props.getProperty(COM_PORT_KEY));
        HandlerContext ctx = inbound.get(comport);
        if (comport <= PORT_COUNT) {
            if (ctx == null) {
                Codec inboundCodec = service.getCodec(CODEC_ID);
                Properties p = new Properties();
                p.put(TcpClientCodecImpl.HOST_KEY, HOST);
                p.put(TcpClientCodecImpl.PORT_KEY, BASE_PORT + comport);
                p.put(TcpClientCodecImpl.WAITE_TIMEOUT_KEY, WAITE_TIMEOUT);

                ctx = inboundCodec.openOutbound(service, p, this);
                inbound.put(comport, ctx);
            } else {
                // already opened.
            }
        }
        return ctx;
    }
}
