package com.wincom.dcim.agentd;

public interface ServerChannelFactory {
    public void create(String host, int port, Acceptor acceptor);
}
