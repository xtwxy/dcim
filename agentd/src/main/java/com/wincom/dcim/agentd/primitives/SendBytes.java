package com.wincom.dcim.agentd.primitives;

import java.nio.ByteBuffer;

/**
 *
 * @author master
 */
public class SendBytes extends Message.Adapter {

    private final ByteBuffer buffer;

    public SendBytes(ByteBuffer o) {
        this.buffer = o;
    }

    public ByteBuffer getByteBuffer() {
        return this.buffer;
    }

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        if (handler instanceof ChannelOutboundHandler) {
            ((ChannelOutboundHandler) handler).handleSendPayload(ctx, this);
        } else {
            handler.handle(ctx, this);
        }
    }

    @Override
    public String toString() {
        return String.format("SendBytes %s", buffer);
    }
}
