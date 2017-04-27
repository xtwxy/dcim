package com.wincom.dcim.agentd;

public interface Codec extends IoCompletionHandler {
    public void encode(Object msg, IoCompletionHandler handler);
    public void decode(Object msg);
    
    public void setInbound(CodecChannel cc);
    public void setOutboundCodec(Codec cc);
    public void setOutboundCodec(String channelId, Codec cc);
    
    public static class Adapter implements Codec {
        private CodecChannel inboundCodecChannel;
        
        @Override
        public void encode(Object msg, IoCompletionHandler handler) {
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
        public void onComplete() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setInbound(CodecChannel cc) {
            this.inboundCodecChannel = cc;
        }

        public CodecChannel getInbound() {
            return inboundCodecChannel;
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
