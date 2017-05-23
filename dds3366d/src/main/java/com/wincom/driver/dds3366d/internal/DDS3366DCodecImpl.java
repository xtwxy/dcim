package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.CodecChannel;
import com.wincom.dcim.agentd.IoCompletionHandler;
import io.netty.buffer.ByteBuf;
import com.wincom.dcim.agentd.Dependency;
import com.wincom.dcim.agentd.primitives.GetSignalValues;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.PushEvents;
import com.wincom.dcim.agentd.primitives.SetSignalValues;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import com.wincom.dcim.agentd.Target;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.statemachine.StateMachine;

/**
 * Composition of TCP connections to a MP3000.
 *
 * @author master
 */
public class DDS3366DCodecImpl implements Codec {

    private CodecChannel inbound;
    private DDS3366DCodecChannelImpl outbound;
    private AgentdService agent;

    private final Map<Class, Handler> handlers;
    private final ConcurrentLinkedDeque<Runnable> queue;

    /**
     *
     * @param agent reference to agent service.
     */
    public DDS3366DCodecImpl(
            AgentdService agent
    ) {
        this.agent = agent;
        this.queue = new ConcurrentLinkedDeque<>();
        this.handlers = new HashMap<>();
        initHandlers(this.handlers);
    }

    @Override
    public void encode(Object msg, IoCompletionHandler handler) {
        Message message = (Message) msg;

        message.apply(null, this.handlers.get(msg));
    }

    @Override
    public void decode(Object msg) {
        if (msg instanceof ByteBuf) {

        } else {

        }
    }

    /**
     * @see Codec#setInbound(com.wincom.dcim.agentd.CodecChannel)
     * @param cc
     */
    @Override
    public void setInbound(CodecChannel cc) {
        this.inbound = cc;
    }

    /**
     * Connect <code>Codec</code> to <code>Codecchannel</code>.
     *
     * @param cc the <code>Codec</code> to be connected.
     */
    @Override
    public void setOutboundCodec(Codec cc) {
        outbound.setOutboundCodec(cc);
    }

    @Override
    public StateMachine withDependencies(com.wincom.dcim.agentd.statemachine.StateMachine sm) {
        return inbound.withDependencies(sm);
    }

    private void dequeue() {
        synchronized (queue) {
            queue.poll();
            Runnable r = queue.peek();
            if (r != null) {
                getInbound().execute(r);
            }
        }
    }

    private void initHandlers(Map<Class, Handler> handler) {
        handler.put(GetSignalValues.Request.class, new Handler() {
            @Override
            public void handle(HandlerContext ctx, Message m) {
                synchronized (queue) {
                    boolean isFirst = false;
                    /* to avoid of using if(queue.size() == 1) test. */
                    if (queue.isEmpty()) {
                        isFirst = true;
                    }

                    queue.add(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                    if (isFirst) {
                        Runnable r = queue.peek();
                        getInbound().execute(r);
                    }
                }
            }
        });
        handler.put(GetSignalValues.Response.class, new Handler() {
            @Override
            public void handle(HandlerContext ctx, Message m) {

            }
        });

        handler.put(SetSignalValues.Request.class, new Handler() {
            @Override
            public void handle(HandlerContext ctx, Message m) {

            }
        });
        handler.put(SetSignalValues.Response.class, new Handler() {
            @Override
            public void handle(HandlerContext ctx, Message m) {

            }
        });

        handler.put(PushEvents.class, new Handler() {
            @Override
            public void handle(HandlerContext ctx, Message m) {

            }
        });
    }
}
