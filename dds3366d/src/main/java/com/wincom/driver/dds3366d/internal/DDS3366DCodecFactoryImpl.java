package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.CodecFactory;
import java.util.Properties;

/**
 * Factory to create MP3000 <code>Codec</code>.
 *
 * @author master
 */
public class DDS3366DCodecFactoryImpl implements CodecFactory {

    private AgentdService agent;

    /**
     * Constructs an <code>MP3000CodecImpl</code> instance with an
     * <code>AgentdService</code>.
     *
     * @param agent
     */
    public DDS3366DCodecFactoryImpl(AgentdService agent) {
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
        return new DDS3366DCodecImpl(agent);
    }

}
