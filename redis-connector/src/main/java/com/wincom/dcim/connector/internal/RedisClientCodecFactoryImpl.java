package com.wincom.dcim.connector.internal;

import java.util.Properties;

import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.CodecFactory;
import com.wincom.dcim.agentd.NetworkService;

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
