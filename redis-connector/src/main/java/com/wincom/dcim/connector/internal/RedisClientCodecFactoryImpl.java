package com.wincom.dcim.connector.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.CodecFactory;
import com.wincom.dcim.agentd.NetworkService;

import java.util.Properties;

/**
 * Created by master on 6/23/17.
 */
public class RedisClientCodecFactoryImpl implements CodecFactory {
    private final NetworkService service;

    public RedisClientCodecFactoryImpl(NetworkService service) {
        this.service = service;
    }
    @Override
    public Codec create(Properties props) {
        return new RedisClientCodecImpl(service, props);
    }
}
