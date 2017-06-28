package com.wincom.dcim.agentd;

import java.util.Properties;
import java.util.Set;

public interface AgentdService {

     void registerCodecFactory(String key, CodecFactory factory);

     void unregisterCodecFactory(String key);

     Set<String> getCodecFactoryKeys();

    /**
     * Create or get a <codec>Codec</codec>. FIXME: this interface design is
     * problem.
     *
     * @param factoryId
     * @param codecId
     * @param props
     * @return
     */
     Codec createCodec(String factoryId, String codecId, Properties props);

     Codec getCodec(String codecId);

     void setCodec(String codecId, Codec codec);
}
