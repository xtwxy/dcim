package com.wincom.dcim.agentd;

import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;

public interface CodecChannel extends Dependency {

    /* Calls */
    public void write(Object msg, ChannelPromise promise);

    public void timeout();

    public void error(Exception e);

    public void close();

    public void execute(Runnable r);

    /* Callbacks */
    public void fireRead(Object msg);

    public void fireClosed();

    public void fireTimeout();

    public void fireError(Exception e);

    public void fireExecutionComplete();

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
        public void write(Object msg, ChannelPromise promise) {
            inboundCodec.encode(msg, promise);
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
        public void fireExecutionComplete() {
            this.outboundCodec.onExecutionComplete();
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
        public Runnable withDependencies(Runnable r) {
            return r;
        }
    }
}
