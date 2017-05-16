package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.CodecFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AgentdServiceImpl implements AgentdService {

    Logger log = LoggerFactory.getLogger(this.getClass());
    private final BundleContext bundleContext;
    private final Map<String, CodecFactory> codecFactories;
    private final Map<String, Codec> codecs;

    public AgentdServiceImpl() {
        this(null);
    }

    public AgentdServiceImpl(BundleContext context) {
        this.bundleContext = context;
        this.codecFactories = new HashMap<>();
        this.codecs = new HashMap<>();
    }

    @Override
    public void registerCodecFactory(String key, CodecFactory factory) {
        this.codecFactories.put(key, factory);
    }

    @Override
    public void unregisterCodecFactory(String key) {
        this.codecFactories.remove(key);
    }

    @Override
    public Codec createCodec(String factoryId, String codecId, Properties props) {
        log.info(String.format("%s", props));
        Codec codec = getCodec(codecId);
        if (codec == null) {
            codec = this.codecFactories.get(factoryId).create(this, props);
            this.codecs.put(codecId, codec);
        }
        return codec;
    }

    @Override
    public Codec getCodec(String codecId) {
        return this.codecs.get(codecId);
    }

    @Override
    public void setCodec(String codecId, Codec codec) {
        this.codecs.put(codecId, codec);
    }
}
