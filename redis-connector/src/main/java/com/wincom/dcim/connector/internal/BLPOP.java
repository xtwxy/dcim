package com.wincom.dcim.connector.internal;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.internal.NetworkServiceImpl;
import com.wincom.dcim.agentd.primitives.GetSignalValues;
import com.wincom.dcim.connector.CompletionHandler;
import com.wincom.dcim.connector.Connection;
import com.wincom.dcim.connector.ConnectionCallback;
import com.wincom.dcim.connector.ConnectionFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by master on 6/19/17.
 */
public class BLPOP {
    private NetworkService service;

    public BLPOP(NetworkService service) {
        this.service = service;
    }

    public static void main(String[] args) {
        NetworkService service = new NetworkServiceImpl(null);
        ConnectionFactoryImpl factory = new ConnectionFactoryImpl(service);
        LPUSH push = new LPUSH(service);

        push.connect(factory);
    }

    public void connect(ConnectionFactory factory) {
        factory.getConnection(new ConnectionCallback() {
            @Override
            public void completed(Throwable cause, Connection c) {
                if (cause == null) {
                    blpop(c);
                }
            }
        });
    }

    public void blpop(Connection c) {
        Set<String> keys = new HashSet<>();
        for (int i = 0; i < 10; ++i) {
            keys.add("key" + i);
        }
        GetSignalValues.Request request = new GetSignalValues.Request(null, keys);
        c.send(request, "queue", new CompletionHandler() {
            @Override
            public void completed() {
                service.getEventLoopGroup().execute(new Runnable() {
                    @Override
                    public void run() {
                        blpop(c);
                    }
                });
            }
        });
    }
}
