package com.wincom.dcim.agentd.primitives;

import java.nio.ByteBuffer;

/**
 *
 * @author master
 */
public class SendBytes implements Message {

    private final ByteBuffer buffer;

    public SendBytes(ByteBuffer o) {
        this.buffer = o;
    }

    @Override
    public void apply(HandlerContext ctx, Handler handler) {
        handler.handle(ctx, this);
    }

    public ByteBuffer getByteBuffer() {
        return this.buffer;
    }

    @Override
    public String toString() {
        return String.format("SendBytes %s", buffer);
    }
}
