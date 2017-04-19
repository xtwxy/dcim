package com.wincom.fsu.mp3000.internal;

import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.CodecChannel;
import java.util.HashMap;
import java.util.Map;

public class MP3000CodecImpl implements Codec {
    Map<String, MP3000CodecChannelImpl> outbound = new HashMap<>();
    
    @Override
    public void encode(Object msg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void decode(Object msg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onTimeout() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onError(Exception e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onClose() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void onExecutionComplete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setInbound(CodecChannel cc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setOutbound(CodecChannel cc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setOutbound(String id, CodecChannel cc) {
        outbound.get(id);
    }
    
}
