package com.wincom.dcim.agentd;

import java.util.Properties;
import java.util.Set;

public interface AgentdService {

    public void registerCodecFactory(String key, CodecFactory factory);

    public void unregisterCodecFactory(String key);

    public Set<String> getCodecFactoryKeys();

    /**
     * Create or get a <codec>Codec</codec>. FIXME: this interface design is
     * problem.
     *
     * @param factoryId
     * @param codecId
     * @param props
     * @return
     */
    public Codec createCodec(String factoryId, String codecId, Properties props);

    public Codec getCodec(String codecId);

    public void setCodec(String codecId, Codec codec);
}
