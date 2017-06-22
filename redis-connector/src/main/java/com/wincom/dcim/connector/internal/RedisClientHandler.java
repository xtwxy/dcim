package com.wincom.dcim.connector.internal;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.redis.*;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by master on 6/19/17.
 */
public class RedisClientHandler extends ChannelDuplexHandler {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        String[] commands = ((String) msg).split("\\s+");
        List<RedisMessage> children = new ArrayList<>(commands.length);
        for(String cmdString : commands) {
            children.add(new FullBulkStringRedisMessage(ByteBufUtil.writeUtf8(ctx.alloc(), cmdString)));
        }
        RedisMessage request = new ArrayRedisMessage(children);
        ctx.write(request, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        RedisMessage redisMessage = (RedisMessage) msg;
        printAggregatedRedisResponse(redisMessage);
        ReferenceCountUtil.release(redisMessage);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.err.print("exception caught: ");
        cause.printStackTrace();
        ctx.close();
    }

    private static void printAggregatedRedisResponse(RedisMessage msg) {
        if(msg instanceof SimpleStringRedisMessage) {
            System.out.println(((SimpleStringRedisMessage)msg).content());
        } else if(msg instanceof ErrorRedisMessage) {
            System.out.println(((ErrorRedisMessage)msg).content());
        } else if(msg instanceof IntegerRedisMessage) {
            System.out.println(((IntegerRedisMessage)msg).value());
        } else if(msg instanceof FullBulkStringRedisMessage) {
            System.out.println(getString((FullBulkStringRedisMessage)msg));
        } else if(msg instanceof ArrayRedisMessage) {
            for(RedisMessage child : ((ArrayRedisMessage)msg).children()) {
                printAggregatedRedisResponse(child);
            }
        } else {

        }
    }

    private static String getString(FullBulkStringRedisMessage msg) {
        if(msg.isNull()) {
            return "(null)";
        }
        return msg.content().toString(CharsetUtil.UTF_8);
    }
}
