package com.wincom.fsu.mp3000.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.CodecFactory;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory to create MP3000 <code>Codec</code>.
 *
 * @author master
 */
public class MP3000CodecFactoryImpl implements CodecFactory {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public static final String OUTBOUND_CODEC_ID_KEY = "codecId";
    public static final String OUTBOUND_CTX_PROPS_KEY = "outboundProps";

    /**
     * Create <code>MP3000CodecImpl</code> instance.
     * 
     * @param service
     * @param props
     * @return 
     */
    @Override
    public Codec create(AgentdService service, Properties props) {

        return new MP3000CodecImpl(
                props.getProperty("host"),
                Integer.parseInt(props.getProperty("basePort")),
                Integer.parseInt(props.getProperty("portCount")),
                service
        );
    }
}
