package com.wincom.dcim.connector;

import com.wincom.dcim.agentd.messages.Message;

/**
 * Created by master on 6/20/17.
 */
public interface Connection {
    public void send(Message msg, String queueName, CompletionHandler handler);
    public Message receive(String queueName);
    public void set(String key, Message msg, String hash, CompletionHandler handler);
    public Message get(String key, String hash);
    public void release();
    public void close();
}
