package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.CodecFactory;
import com.wincom.dcim.agentd.HandlerContext;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory to initial MP3000 <code>Codec</code>.
 *
 * @author master
 */
public class DDS3366DCodecFactoryImpl implements CodecFactory {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public static final String OUTBOUND_CODEC_ID_KEY = "codecId";
    public static final String OUTBOUND_CTX_PROPS_KEY = "outboundProps";

    private final AgentdService agent;

    public DDS3366DCodecFactoryImpl(AgentdService agent) {
        this.agent = agent;
    }
    /**
     * Create <code>DDS3366DCodecImpl</code> instance.
     *
     * @param props properties of this <code>Codec</code>.
     * @return <code>DDS3366DCodecImpl</code> instance.
     */
    @Override
    public Codec create(Properties props) {
        log.debug(props.toString());

        Codec inboundCodec = agent.getCodec(props.getProperty(OUTBOUND_CODEC_ID_KEY));

        HandlerContext outboundHandlerContext = inboundCodec.openInbound(
                (Properties) props.get(OUTBOUND_CTX_PROPS_KEY));
        
        log.info("({}, {})", props, outboundHandlerContext);
        return new DDS3366DCodecImpl(outboundHandlerContext);
    }

}
