package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.CodecFactory;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory to create MP3000 <code>Codec</code>.
 *
 * @author master
 */
public class ModbusCodecFactoryImpl implements CodecFactory {

    Logger log = LoggerFactory.getLogger(this.getClass());
    public static final String OUTBOUND_CODEC_ID_KEY = "codecId";
    public static final String OUTBOUND_CTX_PROPS_KEY = "outboundProps";

    /**
     * Create <code>MP3000CodecImpl</code> instance.
     *
     * @param props properties of this <code>Codec</code>.
     * @return <code>MP3000CodecImpl</code> instance.
     */
    @Override
    public Codec create(AgentdService service, Properties props) {
        log.info(props.toString());

        Codec inboundCodec = service.getCodec(props.getProperty(OUTBOUND_CODEC_ID_KEY));

        Codec theCodec = new ModbusCodecImpl();
        HandlerContext outboundContext = inboundCodec.openOutbound(service,
                (Properties) props.get(OUTBOUND_CTX_PROPS_KEY),
                theCodec);

        return theCodec;
    }

}
