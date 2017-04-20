package com.wincom.fsu.mp3000.internal;

import com.wincom.dcim.agentd.Codec;
import java.util.HashMap;
import java.util.Map;

/**
 * Composition of TCP connections to a MP3000.
 * @author master
 */
public class MP3000CodecImpl extends Codec.Adapter {
    Map<String, MP3000CodecChannelImpl> outbound = new HashMap<>();
    
    /**
     * Connect <code>Codec</code> to <code>Codecchannel</code> 
     * with identifier <code>channelId</code>.
     * 
     * @param channelId the identifier of the <code>CodecChannel</code>.
     * @param cc the <code>Codec</codec> to be connected.
     */
    @Override
    public void setOutboundCodec(String channelId, Codec cc) {
        outbound.get(channelId).setCodec(cc);
    }

    @Override
    public void onWriteComplete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
