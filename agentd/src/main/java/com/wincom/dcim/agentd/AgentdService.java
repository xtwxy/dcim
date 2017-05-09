package com.wincom.dcim.agentd;

public interface AgentdService {

    public void registerCodecFactory(String key, CodecFactory factory);

    public void unregisterCodecFactory(String key);
}
