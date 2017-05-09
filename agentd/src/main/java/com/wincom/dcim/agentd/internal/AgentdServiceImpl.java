package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.CodecFactory;
import java.util.HashMap;
import java.util.Map;
import org.osgi.framework.BundleContext;

public final class AgentdServiceImpl implements AgentdService {

    private BundleContext bundleContext;
    private Map<String, CodecFactory> codecFactories;

    public AgentdServiceImpl(BundleContext context) {
        this.bundleContext = context;
        this.codecFactories = new HashMap<>();
    }

    @Override
    public void registerCodecFactory(String key, CodecFactory factory) {
        this.codecFactories.put(key, factory);
    }

    @Override
    public void unregisterCodecFactory(String key) {
        this.codecFactories.remove(key);
    }
}
