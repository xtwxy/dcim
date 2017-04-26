package com.wincom.dcim.agentd;

public interface Codec {
    public void encode(Object msg, Runnable promise);
    public void decode(Object msg);
    public void onTimeout();
    public void onError(Exception e);
    public void onClose();
    public void onExecutionComplete();
    
    public void setInbound(CodecChannel cc);
    public void setOutboundCodec(Codec cc);
    public void setOutboundCodec(String channelId, Codec cc);
    
    public static class Adapter implements Codec {
        private CodecChannel inboundCodecChannel;
        
        @Override
        public void encode(Object msg, Runnable promise) {
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
            this.inboundCodecChannel = cc;
        }

        @Override
        public void setOutboundCodec(Codec cc) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setOutboundCodec(String channelId, Codec cc) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
    }
}
