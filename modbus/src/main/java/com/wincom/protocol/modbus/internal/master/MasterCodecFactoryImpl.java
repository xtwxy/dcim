package com.wincom.protocol.modbus.internal.master;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.CodecFactory;
import com.wincom.dcim.agentd.HandlerContext;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory to create MP3000 <code>Codec</code>.
 *
 * @author master
 */
public class MasterCodecFactoryImpl implements CodecFactory {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public static final String OUTBOUND_CODEC_ID_KEY = "codecId";
    public static final String OUTBOUND_CTX_PROPS_KEY = "outboundProps";

    private final AgentdService agent;
    public MasterCodecFactoryImpl(AgentdService agent) {
        this.agent = agent;
    }
    /**
     * Create <code>MP3000CodecImpl</code> instance.
     *
     * @param props properties of this <code>Codec</code>.
     * @return <code>MP3000CodecImpl</code> instance.
     */
    @Override
    public Codec create(Properties props) {
        log.debug(props.toString());

        Codec outboundCodec = agent.getCodec(props.getProperty(OUTBOUND_CODEC_ID_KEY));

        HandlerContext outboundHandlerContext = outboundCodec.openInbound(
                (Properties) props.get(OUTBOUND_CTX_PROPS_KEY));
        
        return new MasterCodecImpl(outboundHandlerContext);
    }

}
