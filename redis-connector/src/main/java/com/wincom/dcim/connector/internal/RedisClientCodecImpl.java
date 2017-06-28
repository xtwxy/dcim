package com.wincom.dcim.connector.internal;

import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.messages.Connect;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Created by master on 6/23/17.
 */
public class RedisClientCodecImpl implements Codec {

    final String HOST;
    final int PORT;
    final String PASSWORD;
    final int MAX_CONNECTION_COUNT;

    private final NetworkService service;
    private final List<HandlerContext> available;
    private final List<HandlerContext> used;

    public RedisClientCodecImpl(NetworkService service, Properties props) {
        this.HOST = props.getProperty("host", "127.0.0.1");
        this.PORT = Integer.parseInt(props.getProperty("port", "6379"));
        this.PASSWORD = props.getProperty("password", "foobared");
        this.MAX_CONNECTION_COUNT = Integer.parseInt(props.getProperty("poolsize", "1000"));

        this.service = service;
        this.available = new LinkedList<>();
        this.used = new LinkedList<>();
    }

    @Override
    public synchronized HandlerContext openInbound(Properties props) {
        HandlerContext ctx = null;
        if(available.isEmpty()) {
            ctx = new RedisClientHandlerContextImpl(service, this);
            ctx.send(new Connect(ctx, HOST, PORT));
            used.add(ctx);
        } else {
            ctx = available.remove(0);
            used.add(ctx);
        }

        return ctx;
    }

    synchronized void release(RedisClientHandlerContextImpl ctx) {
        used.remove(ctx);
        if(available.size() < MAX_CONNECTION_COUNT) {
            available.add(ctx);
        } else {
            ctx.dispose();
        }
    }
}
