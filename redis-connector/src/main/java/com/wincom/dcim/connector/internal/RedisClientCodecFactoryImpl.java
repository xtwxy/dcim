package com.wincom.dcim.connector.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.CodecFactory;

import java.util.Properties;

/**
 * Created by master on 6/23/17.
 */
public class RedisClientCodecFactoryImpl implements CodecFactory {
    @Override
    public Codec create(AgentdService service, Properties props) {
        return new RedisClientCodecImpl(service, props);
    }
}
