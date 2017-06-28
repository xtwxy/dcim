package com.wincom.dcim.connector.internal;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.NetworkService;

import io.netty.channel.Channel;

/**
 * Created by master on 6/23/17.
 */
public class RedisClientHandlerContextImpl extends HandlerContext.Adapter {
    final private RedisClientCodecImpl codec;

    RedisClientHandlerContextImpl(NetworkService service, RedisClientCodecImpl codec) {
        this.codec = codec;
        this.inboundHandler = new RedisClientInboundHandlerImpl(service, codec.PASSWORD);
        this.outboundHandler = new RedisClientOutboundHandlerImpl(service);
    }
    public void setChannel(Channel channel) {
        ((RedisClientOutboundHandlerImpl) outboundHandler).setChannel(channel);
        ((RedisClientInboundHandlerImpl) inboundHandler).setChannel(channel);
    }

    public void release() {
        codec.release(this);
    }
}
