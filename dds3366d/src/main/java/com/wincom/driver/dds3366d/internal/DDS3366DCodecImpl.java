package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.CodecChannel;
import com.wincom.dcim.agentd.IoCompletionHandler;
import io.netty.buffer.ByteBuf;
import com.wincom.dcim.agentd.Dependable;
import com.wincom.dcim.agentd.Dependency;
import com.wincom.dcim.agentd.primitives.GetSignalValues;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SetSignalValues;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Composition of TCP connections to a MP3000.
 *
 * @author master
 */
public class DDS3366DCodecImpl extends Codec.Adapter implements Dependable {

    private CodecChannel inbound;
    private DDS3366DCodecChannelImpl outbound;
    private AgentdService agent;

    private final Handler handler;
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
        this.handler = new Handler.Adapter() {
            @Override
            public void handleGetSignalValuesRequest(GetSignalValues.Request r) {

            }

            @Override
            public void handleGetSignalValuesRequest(GetSignalValues.Response r) {
                outbound.fireRead(r);
            }

            @Override
            public void handleSetSignalValuesRequest(SetSignalValues.Request r) {

            }

            @Override
            public void handleSetSignalValuesResponse(SetSignalValues.Response r) {
                outbound.fireRead(r);
            }
        };
    }

    @Override
    public void encode(Object msg, IoCompletionHandler handler) {
        Message message = (Message) msg;
                
        
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
    public Dependency withDependencies(Dependency r) {
        return inbound.withDependencies(r);
    }

    private void dequeue() {
        synchronized (queue) {
            queue.poll();
            Runnable r = queue.peek();
            if(r != null) {
                getInbound().execute(r);
            }
        }
    }
}
