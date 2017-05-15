package com.wincom.dcim.agentd.internal.mocks;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.CodecFactory;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import static java.lang.System.out;
import java.util.Properties;

/**
 *
 * @author master
 */
public class CodecFactoryImpl implements CodecFactory {

    public static final String CODEC_ID_KEY = "codecId";
    public static final String OUTBOUND_PROPS_KEY = "outboundProps";

    @Override
    public Codec create(AgentdService service, Properties props) {
        out.println(props);
        
        Codec inboundCodec = service.getCodec(props.getProperty(CODEC_ID_KEY));
        HandlerContext inboundContext = inboundCodec.createOutbound(service, (Properties) props.get(OUTBOUND_PROPS_KEY));
        Codec theCodec = new CodecImpl(inboundContext);

        return theCodec;
    }
}
