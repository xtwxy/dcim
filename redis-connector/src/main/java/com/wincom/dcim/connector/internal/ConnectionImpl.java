package com.wincom.dcim.connector.internal;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.sun.jersey.api.json.JSONJAXBContext;
import com.sun.jersey.api.json.JSONMarshaller;
import com.wincom.dcim.agentd.config.MyJAXBContextResolver;
import com.wincom.dcim.agentd.json.RequestConverter;
import com.wincom.dcim.agentd.messages.Handler;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.primitives.RequestMessage;
import com.wincom.dcim.connector.CompletionHandler;
import com.wincom.dcim.connector.Connection;
import com.wincom.dcim.connector.ConnectionFactory;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * Created by master on 6/20/17.
 */
public class ConnectionImpl implements Connection {
    private final Channel channel;
    private final ConnectionFactory factory;
    private final MyJAXBContextResolver resolver = new MyJAXBContextResolver();

    public ConnectionImpl(Channel ch, ConnectionFactory factory) {
        this.channel = ch;
        this.factory = factory;
    }

    @Override
    public void send(Message msg, String queueName, CompletionHandler handler) {
        String json = requestToJson((RequestMessage) msg);
        ChannelPromise promise = channel.newPromise();
        promise.addListener(new GenericFutureListener<ChannelFuture>() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                handler.completed();
            }
        });
        channel.writeAndFlush(String.format("lpush %s %s", queueName, json), promise);
    }

    @Override
    public Message receive(String queueName, int timeoutSeconds, CompletionHandler handler) {
        ChannelPromise promise = channel.newPromise();
        promise.addListener(new GenericFutureListener<ChannelFuture>() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                handler.completed();
            }
        });
        channel.writeAndFlush(String.format("brpop %s %s", queueName, timeoutSeconds));
        return null;
    }

    @Override
    public Message listen(String queueName, int timeoutSeconds, Handler handler) {
        return null;
    }

    @Override
    public void set(String key, Message msg, String hash, CompletionHandler handler) {

    }

    @Override
    public Message get(String key, String hash, CompletionHandler handler) {
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

    private String requestToJson(RequestMessage msg) {
        try {
            RequestConverter converter = new RequestConverter(msg);
            JAXBContext context = resolver.getContext(converter.getClass());
            Marshaller m = context.createMarshaller();
            JSONMarshaller marshaller = JSONJAXBContext.getJSONMarshaller(m, context);
            StringWriter out = new StringWriter();
            marshaller.marshallToJSON(converter, out);
            return out.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
