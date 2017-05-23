package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.CodecFactory;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory to initial MP3000 <code>Codec</code>.
 *
 * @author master
 */
public class DDS3366DCodecFactoryImpl implements CodecFactory {

    Logger log = LoggerFactory.getLogger(this.getClass());
    public static final String OUTBOUND_CODEC_ID_KEY = "codecId";
    public static final String OUTBOUND_CTX_PROPS_KEY = "outboundProps";

    /**
     * Create <code>DDS3366DCodecImpl</code> instance.
     *
     * @param props properties of this <code>Codec</code>.
     * @return <code>DDS3366DCodecImpl</code> instance.
     */
    @Override
    public Codec create(AgentdService service, Properties props) {
        log.info(props.toString());

        Codec inboundCodec = service.getCodec(props.getProperty(OUTBOUND_CODEC_ID_KEY));

        Codec theCodec = new DDS3366DCodecImpl();
        HandlerContext outboundContext = inboundCodec.openInbound(service,
                (Properties) props.get(OUTBOUND_CTX_PROPS_KEY),
                theCodec);

        return theCodec;
    }

}
