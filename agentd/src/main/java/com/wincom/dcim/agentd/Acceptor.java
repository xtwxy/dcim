package com.wincom.dcim.agentd;

import io.netty.channel.socket.SocketChannel;

public interface Acceptor {
    public void onAccept(SocketChannel ch);
}
