package com.wincom.protocol.modbus.internal.mocks;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.CodecFactory;
import com.wincom.dcim.agentd.HandlerContext;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class CodecFactoryImpl implements CodecFactory {

    Logger log = LoggerFactory.getLogger(this.getClass());
    public static final String OUTBOUND_CODEC_ID_KEY = "codecId";
    public static final String OUTBOUND_CTX_PROPS_KEY = "outboundProps";

    @Override
    public Codec create(AgentdService service, Properties props) {
        log.info(props.toString());

        Codec inboundCodec = service.getCodec(props.getProperty(OUTBOUND_CODEC_ID_KEY));

        HandlerContext outboundHandlerContext = inboundCodec.openInbound(service, 
                (Properties) props.get(OUTBOUND_CTX_PROPS_KEY));

        return new CodecImpl(outboundHandlerContext);
    }
}
