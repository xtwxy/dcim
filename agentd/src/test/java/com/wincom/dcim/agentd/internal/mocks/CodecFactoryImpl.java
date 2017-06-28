package com.wincom.dcim.agentd.internal.mocks;

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

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public static final String OUTBOUND_CODEC_ID_KEY = "codecId";
    public static final String OUTBOUND_CTX_PROPS_KEY = "outboundProps";

    private final AgentdService agent;

    public CodecFactoryImpl(AgentdService agent) {
        this.agent = agent;
    }
    @Override
    public Codec create(Properties props) {
        log.info(props.toString());

        Codec outboundCodec = agent.getCodec(props.getProperty(OUTBOUND_CODEC_ID_KEY));

        HandlerContext outboundHandlerContext = outboundCodec.openInbound(
                (Properties) props.get(OUTBOUND_CTX_PROPS_KEY));

        return new CodecImpl(agent, outboundHandlerContext);
    }
}
