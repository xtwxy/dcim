package com.wincom.dcim.agentd;

public interface ServerChannelFactory {
    public Channel create(String host, int port);
}
