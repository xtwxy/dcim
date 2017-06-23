package com.wincom.dcim.connector;

import com.sun.xml.internal.ws.api.handler.MessageHandler;
import com.wincom.dcim.agentd.messages.Handler;
import com.wincom.dcim.agentd.messages.Message;

/**
 * Created by master on 6/20/17.
 */
public interface Connection {
    public void send(Message msg, String queueName, CompletionHandler handler);
    public Message receive(String queueName, int timeoutSeconds, CompletionHandler handler);
    public Message listen(String queueName, int timeoutSeconds, Handler handler);
    public void set(String key, Message msg, String hash, CompletionHandler handler);
    public Message get(String key, String hash, CompletionHandler handler);
    public void release();
    public void close();
}
