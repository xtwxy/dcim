package com.wincom.dcim.connector.internal;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.internal.TcpInboundHandlerImpl;
import com.wincom.dcim.agentd.internal.TcpOutboundHandlerImpl;
import io.netty.channel.Channel;

/**
 * Created by master on 6/23/17.
 */
public class RedisClientHandlerContextImpl extends HandlerContext.Adapter {
    final private RedisClientCodecImpl codec;
    private Channel channel;
    private final NetworkService service;

    RedisClientHandlerContextImpl(NetworkService service, RedisClientCodecImpl codec) {
        this.service = service;
        this.codec = codec;
        this.inboundHandler = new RedisClientInboundHandlerImpl(service, codec.PASSWORD);
        this.outboundHandler = new RedisClientOutboundHandlerImpl(service);
    }
    public void setChannel(Channel channel) {
        this.channel = channel;
        ((RedisClientOutboundHandlerImpl) outboundHandler).setChannel(channel);
        ((RedisClientInboundHandlerImpl) inboundHandler).setChannel(channel);
    }

    public void release() {
        codec.release(this);
    }
}
