package com.wincom.dcim.agentd;

import io.netty.channel.ChannelFuture;

public interface ServerChannelFactory {
    public ChannelFuture create(String host, int port, Acceptor acceptor);
}
