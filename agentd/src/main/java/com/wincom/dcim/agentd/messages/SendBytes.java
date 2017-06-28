package com.wincom.dcim.agentd.messages;

import java.nio.ByteBuffer;

import com.wincom.dcim.agentd.ChannelOutboundHandler;
import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public final class SendBytes extends ChannelOutbound implements Wireable {

    private final ByteBuffer buffer;

    public SendBytes(HandlerContext c, ByteBuffer o) {
        super(c);
        this.buffer = o;
    }

    public ByteBuffer getByteBuffer() {
        return this.buffer;
    }

    @Override
    public void applyChannelOutbound(HandlerContext ctx, ChannelOutboundHandler handler) {
        handler.handleSendPayload(ctx, this);
    }

    @Override
    public int getWireLength() {
        return buffer.remaining();
    }

    @Override
    public void toWire(ByteBuffer buffer) {
        buffer.put(this.buffer);
    }

    @Override
    public void fromWire(ByteBuffer buffer) {
        this.buffer.put(buffer);
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        AbstractWireable.appendHeader(buf, depth, getClass().getSimpleName());
        depth++;
        AbstractWireable.appendValue(buf, depth, "Buffer", buffer);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        toStringBuilder(buf, 0);
        return buf.toString();
    }
}
