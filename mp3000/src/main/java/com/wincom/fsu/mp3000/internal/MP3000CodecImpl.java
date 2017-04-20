package com.wincom.fsu.mp3000.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import java.util.HashMap;
import java.util.Map;

/**
 * Composition of TCP connections to a MP3000.
 *
 * @author master
 */
public class MP3000CodecImpl extends Codec.Adapter {

    private String HOST;
    private final int BASE_PORT;
    private final int PORT_COUNT;
    private final Map<Integer, MP3000CodecChannelImpl> outbound;

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

    /**
     * Connect <code>Codec</code> to <code>Codecchannel</code> with identifier
     * <code>channelId</code>.
     *
     * @param channelId the identifier of the <code>CodecChannel</code>.
     * @param cc the <code>Codec</codec> to be connected.
     */
    @Override
    public void setOutboundCodec(String channelId, Codec cc) {
        int port = Integer.parseInt(channelId);
        if (port >= 0 && port < this.PORT_COUNT) {
            outbound.get(port).setCodec(cc);
        }
    }
}
