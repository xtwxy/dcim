package com.wincom.fsu.mp3000.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.NetworkConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Composition of TCP connections to a MP3000. this CODEC does not handle
 * inbound messages. just pass request to underlying TCP stream.
 *
 * @author master
 */
public class MP3000CodecImpl implements Codec {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final String HOST;
    private final int BASE_PORT;
    private final int PORT_COUNT;
    private final String WAITE_TIMEOUT;
    private final String CODEC_ID;
    private static final String COM_PORT_KEY = "port";
    private static final String OUTBOUND_CODEC_ID_KEY = "codecId";
    public static final String OUTBOUND_CTX_PROPS_KEY = "outboundProps";

    private final Map<Integer, HandlerContext> inbound;
    private final AgentdService agent;

    public MP3000CodecImpl(AgentdService agent, Properties props) {
        this.agent = agent;
        this.inbound = new HashMap<>();
        this.HOST = props.getProperty("host");
        this.BASE_PORT = Integer.parseInt(props.getProperty("basePort"));
        this.PORT_COUNT = Integer.parseInt(props.getProperty("portCount"));
        this.WAITE_TIMEOUT = props.getProperty(NetworkConfig.WAITE_TIMEOUT_KEY);
        this.CODEC_ID = props.getProperty(OUTBOUND_CODEC_ID_KEY);
    }

    @Override
    public HandlerContext openInbound(
            Properties props) {
        Integer comport = Integer.valueOf(props.getProperty(COM_PORT_KEY));
        HandlerContext ctx = inbound.get(comport);
        if (comport <= PORT_COUNT) {
            if (ctx == null) {
                Codec inboundCodec = agent.getCodec(CODEC_ID);
                Properties p = new Properties();
                p.setProperty(NetworkConfig.HOST_KEY, HOST);
                p.setProperty(NetworkConfig.PORT_KEY, Integer.toString(BASE_PORT + comport));
                p.setProperty(NetworkConfig.WAITE_TIMEOUT_KEY, WAITE_TIMEOUT);

                log.info(p.toString());
                ctx = inboundCodec.openInbound(p);
                inbound.put(comport, ctx);
            } else {
                // already opened.
            }
        }
        return ctx;
    }
}
