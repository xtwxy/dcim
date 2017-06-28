package com.wincom.dcim.agentd.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.CodecFactory;

public final class AgentdServiceImpl implements AgentdService {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private final Map<String, CodecFactory> codecFactories;
    private final Map<String, Codec> codecs;

    public AgentdServiceImpl() {
        this(null);
    }

    public AgentdServiceImpl(BundleContext context) {
        this.codecFactories = new HashMap<>();
        this.codecs = new HashMap<>();
    }

    @Override
    public void registerCodecFactory(String key, CodecFactory factory) {
        log.info(String.format("%s: %s => %s", this, key, factory));
        this.codecFactories.put(key, factory);
    }

    @Override
    public void unregisterCodecFactory(String key) {
        this.codecFactories.remove(key);
    }

    @Override
    public Set<String> getCodecFactoryKeys() {
        return this.codecFactories.keySet();
    }

    @Override
    public Codec createCodec(String factoryId, String codecId, Properties props) {
        log.info(String.format("%s", props));
        Codec codec = getCodec(codecId);
        if (codec == null) {
            CodecFactory factory = codecFactories.get(factoryId);
            log.info(String.format("%s => %s", factoryId, factory));
            codec = this.codecFactories.get(factoryId).create(props);
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

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("codecFactories:");
        for (Map.Entry<String, CodecFactory> e : codecFactories.entrySet()) {
            sb.append(String.format("(%s, %s)\n", e.getKey(), e.getValue()));
        };

        sb.append("codecs:");
        for (Map.Entry<String, Codec> e : codecs.entrySet()) {
            sb.append(String.format("(%s, %s)\n", e.getKey(), e.getValue()));
        }

        return sb.toString();
    }
}
