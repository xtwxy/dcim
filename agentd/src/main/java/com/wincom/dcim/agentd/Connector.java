package com.wincom.dcim.agentd;

import io.netty.channel.Channel;

public interface Connector {
    public void onConnect(Channel ch);
}
