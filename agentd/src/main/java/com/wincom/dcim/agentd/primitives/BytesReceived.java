package com.wincom.dcim.agentd.primitives;

import java.nio.ByteBuffer;

/**
 *
 * @author master
 */
public class BytesReceived extends ChannelInbound {

    private final ByteBuffer buffer;

    public BytesReceived(HandlerContext c, ByteBuffer o) {
        super(c);
        this.buffer = o;
    }

    public ByteBuffer getByteBuffer() {
        return this.buffer;
    }

    @Override
    public void applyChannelInbound(HandlerContext ctx, ChannelInboundHandler handler) {
        handler.handlePayloadReceived(ctx, this);
    }

    @Override
    public String toString() {
        return String.format("BytesReceived %s, %s", getContext(), buffer);
    }

    @Override
    public boolean isOob() {
        return false;
    }
}
