package com.wincom.fsu.mp3000.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.CodecFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

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
        log.debug(props.toString());

        return new MP3000CodecImpl(
                agent,
                props
        );
    }
}
