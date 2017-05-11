package com.wincom.dcim.agentd.primitives;

import java.nio.ByteBuffer;

/**
 *
 * @author master
 */
public class BytesReceived implements Message {

    private final ByteBuffer buffer;

    public BytesReceived(ByteBuffer o) {
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
        return String.format("BytesReceived %s", buffer);
    }
}
