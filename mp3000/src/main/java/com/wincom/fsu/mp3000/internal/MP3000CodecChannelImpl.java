package com.wincom.fsu.mp3000.internal;

import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.CodecChannel;
import com.wincom.dcim.agentd.Connector;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import io.netty.channel.DefaultChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class MP3000CodecChannelImpl implements CodecChannel, Connector {
    Channel channel;
    Codec codec;
    
    @Override
    public void write(Object msg) {
        ChannelPromise promise = new DefaultChannelPromise(channel);
        promise.addListener(new GenericFutureListener() {
            @Override
            public void operationComplete(Future f) throws Exception {
                fireWriteComplete();
            }
        });
        channel.writeAndFlush(msg, promise);
    }

    @Override
    public void timeout() {
        // ignore.
    }

    @Override
    public void error(Exception e) {
        // ignore.
    }

    @Override
    public void close() {
        this.channel.close();
        this.channel = null;
    }

    @Override
    public void execute(Runnable r) {
        // schedule it in the execution group?
        throw new UnsupportedOperationException("Not supported yet.");
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
    public void fireExecute() {
        this.codec.onExecutionComplete();
    }

    @Override
    public void onConnect(Channel ch) {
        if(this.channel != null) {
            this.channel.close();
        }
        this.channel = ch;
    }

    @Override
    public void fireWriteComplete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setCodec(Codec c) {
        if(this.codec != null) {
            codec.setInbound(null);
        }
        this.codec = c;
        c.setInbound(this);
    }
}
