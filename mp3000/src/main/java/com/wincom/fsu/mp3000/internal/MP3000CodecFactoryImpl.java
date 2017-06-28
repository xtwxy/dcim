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
    private final AgentdService agent;

    public MP3000CodecFactoryImpl(AgentdService agent) {
        this.agent = agent;
    }
    /**
     * Create <code>MP3000CodecImpl</code> instance.
     *
     * @param props
     * @return
     */
    @Override
    public Codec create(Properties props) {
        log.info(props.toString());

        Codec theCodec
                = new MP3000CodecImpl(
                agent,
                        props
                );

        return theCodec;
    }
}
