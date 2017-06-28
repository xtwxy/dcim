package com.wincom.dcim.connector;

import com.wincom.dcim.agentd.messages.Handler;
import com.wincom.dcim.agentd.messages.Message;

/**
 * Created by master on 6/20/17.
 */
public interface Connection {
    void send(Message msg, String queueName, CompletionHandler handler);
    Message receive(String queueName, int timeoutSeconds, CompletionHandler handler);
    Message listen(String queueName, int timeoutSeconds, Handler handler);
    void set(String key, Message msg, String hash, CompletionHandler handler);
    Message get(String key, String hash, CompletionHandler handler);
    void release();
    void close();
}
