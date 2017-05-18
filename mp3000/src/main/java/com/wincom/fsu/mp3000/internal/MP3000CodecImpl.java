package com.wincom.fsu.mp3000.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Composition of TCP connections to a MP3000.
 * this CODEC does not handle inbound messages. just pass request to underlying
 * TCP stream.
 * @author master
 */
public class MP3000CodecImpl implements Codec {

    private String HOST;
    private final int BASE_PORT;
    private final int PORT_COUNT;
    private final Map<Integer, MP3000CodecChannelImpl> outbound;

    /**
     * 
     * @param host host or IP address of MP3000.
     * @param basePort the start TCP port number. 
     * @param portCount number of ports.
     * @param agent reference to agent service.
     */
    public MP3000CodecImpl(
            String host,
            int basePort,
            int portCount,
            AgentdService agent
    ) {
        this.outbound = new HashMap<>();
        this.HOST = host;
        this.BASE_PORT = basePort;
        this.PORT_COUNT = portCount;

        for (int i = 0; i < this.PORT_COUNT; ++i) {
            outbound.put(i,
                    new MP3000CodecChannelImpl(
                            this.HOST,
                            this.BASE_PORT + i,
                            agent
                    )
            );
        }
    }

    @Override
    public void codecActive(HandlerContext outboundContext) {
        // this codec does not handle inbound messages - ignore.
    }

    @Override
    public HandlerContext createInbound(AgentdService service, Properties outbound, Handler inboundHandler) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        // this codec does not handle inbound messages - ignore.
    }
}
