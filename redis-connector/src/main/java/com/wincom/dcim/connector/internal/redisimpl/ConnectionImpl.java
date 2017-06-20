package com.wincom.dcim.connector.internal.redisimpl;

import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.connector.Connection;
import com.wincom.dcim.connector.ConnectionFactory;
import io.netty.channel.Channel;

/**
 * Created by master on 6/20/17.
 */
public class ConnectionImpl implements Connection {
    private final Channel channel;
    private final ConnectionFactory factory;
    public ConnectionImpl(Channel ch, ConnectionFactory factory) {
        this.channel = ch;
        this.factory = factory;
    }

    @Override
    public void send(Message msg, String queueName) {

    }

    @Override
    public Message receive(String queueName) {
        return null;
    }

    @Override
    public void set(String key, Message msg, String hash) {

    }

    @Override
    public Message get(String key, String hash) {
        return null;
    }

    @Override
    public void release() {
        factory.release(this);
    }

    @Override
    public void close() {
        channel.close();
    }
}
