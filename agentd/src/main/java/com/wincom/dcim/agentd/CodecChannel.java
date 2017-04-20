package com.wincom.dcim.agentd;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public interface CodecChannel {

    /* Calls */
    public void write(Object msg);

    public void timeout();

    public void error(Exception e);

    public void close();

    public void execute(Runnable r);

    /* Callbacks */
    public void fireWriteComplete();

    public void fireRead(Object msg);

    public void fireClosed();

    public void fireTimeout();

    public void fireError(Exception e);

    public void fireExecutionComplete();

    public static class Adapter implements CodecChannel {

        private Channel channel;
        private Codec codec;
        private EventLoopGroup eventLoopGroup;
        
        /**
         * Construct an <code>CodecChannel</code> instance 
         * with an <code>EventLoop</code>.
         * @param eventLoop must not null!
         */
        public Adapter(EventLoopGroup eventLoopGroup) {
            this.eventLoopGroup = eventLoopGroup;
        }
        
        @Override
        public void write(Object msg) {
            ChannelPromise promise = new DefaultChannelPromise(this.channel);
            promise.addListener(new GenericFutureListener() {
                @Override
                public void operationComplete(Future f) throws Exception {
                    if(f.isSuccess()) {
                        fireWriteComplete();
                    } else {
                        fireError(new RuntimeException("Operation failed."));
                    }
                }
            });
            channel.writeAndFlush(msg, promise);
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
            this.channel.close();
            this.channel = null;
        }

        @Override
        public void execute(Runnable r) {
            this.eventLoopGroup.submit(r);
        }

        @Override
        public void fireRead(Object msg) {
            this.codec.decode(msg);
        }

        @Override
        public void fireClosed() {
            this.codec.onClose();
        }

        @Override
        public void fireTimeout() {
            this.codec.onTimeout();
        }

        @Override
        public void fireError(Exception e) {
            this.codec.onError(e);
        }

        @Override
        public void fireExecutionComplete() {
            this.codec.onExecutionComplete();
        }

        @Override
        public void fireWriteComplete() {
            this.codec.onWriteComplete();
        }

        public void setCodec(Codec c) {
            if (this.codec != null) {
                codec.setInbound(null);
            }
            this.codec = c;
            c.setInbound(this);
        }

        public Channel getChannel() {
            return channel;
        }

        public void setChannel(Channel channel) {
            this.channel = channel;
        }

        public EventLoopGroup getEventLoopGroup() {
            return eventLoopGroup;
        }
    }
}
