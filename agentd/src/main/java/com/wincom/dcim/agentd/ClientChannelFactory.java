package com.wincom.dcim.agentd;

public interface ClientChannelFactory {
    public void create(String host, int port, Connector connector);
}