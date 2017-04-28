package com.wincom.dcim.agentd;

import io.netty.channel.EventLoopGroup;

public interface CodecChannel extends IoCompletionNotifier, Dependable {

    /* Calls */
    public void write(Object msg, IoCompletionHandler handler);

    public void timeout();

    public void error(Exception e);

    public void close();

    public void execute(Runnable r);


    public static class Adapter implements CodecChannel {

        private Codec inboundCodec;
        private Codec outboundCodec;
        private final EventLoopGroup eventLoopGroup;
        
        /**
         * Construct an <code>CodecChannel</code> instance 
         * with an <code>EventLoop</code>.
         * <code>eventLoop</code> must not null!
         * @param eventLoopGroup
         */
        public Adapter(EventLoopGroup eventLoopGroup) {
            this.eventLoopGroup = eventLoopGroup;
        }
        
        @Override
        public void write(Object msg, IoCompletionHandler handler) {
            inboundCodec.encode(msg, handler);
        }

        @Override
        public void timeout() {
            // ignore. downstream timout not supported.
        }

        @Override
        public void error(Exception e) {
            // ignore. downstream error not supported.
        }

        @Override
        public void close() {
            // ignore. 
        }

        @Override
        public void execute(Runnable r) {
            this.eventLoopGroup.submit(r);
        }

        @Override
        public void fireRead(Object msg) {
            this.outboundCodec.decode(msg);
        }

        @Override
        public void fireClosed() {
            this.outboundCodec.onClose();
        }

        @Override
        public void fireTimeout() {
            this.outboundCodec.onTimeout();
        }

        @Override
        public void fireError(Exception e) {
            this.outboundCodec.onError(e);
        }

        @Override
        public void fireComplete() {
            this.outboundCodec.onComplete();
        }

        public EventLoopGroup getEventLoopGroup() {
            return eventLoopGroup;
        }

        public void setOutboundCodec(Codec c) {
            if (this.outboundCodec != null) {
                outboundCodec.setInbound(null);
            }
            this.outboundCodec = c;
            c.setInbound(this);
        }

        public Codec getInboundCodec() {
            return inboundCodec;
        }

        public void setInboundCodec(Codec inboundCodec) {
            this.inboundCodec = inboundCodec;
        }

        @Override
        public Dependency withDependencies(Dependency r) {
            return r;
        }
    }
}
