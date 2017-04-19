package com.wincom.dcim.agentd;

public interface Codec {
    public void encode(Object msg);
    public void decode(Object msg);
    public void onTimeout();
    public void onError(Exception e);
    public void onClose();
    public void onExecutionComplete();
    
    public void setInbound(CodecChannel cc);
    public void setOutbound(CodecChannel cc);
    public void setOutbound(String id, CodecChannel cc);
}
