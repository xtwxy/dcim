package com.wincom.dcim.agentd.messages;

import com.wincom.dcim.agentd.ChannelOutboundHandler;
import com.wincom.dcim.agentd.HandlerContext;

import java.nio.ByteBuffer;

/**
 * @author master
 */
public final class SendString extends ChannelOutbound implements Wireable {

    private String string;

    public SendString(HandlerContext c, String o) {
        super(c);
        this.string = o;
    }

    public String getString() {
        return this.string;
    }

    @Override
    public void applyChannelOutbound(HandlerContext ctx, ChannelOutboundHandler handler) {
        handler.handleSendPayload(ctx, this);
    }

    @Override
    public int getWireLength() {
        return string.length();
    }

    @Override
    public void toWire(ByteBuffer buffer) {
        buffer.put(this.string.getBytes());
    }

    @Override
    public void fromWire(ByteBuffer buffer) {
        byte[] ba = new byte[buffer.remaining()];
        buffer.get(ba);
        this.string = new String(ba);
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        AbstractWireable.appendHeader(buf, depth, getClass().getSimpleName());
        depth++;
        AbstractWireable.appendValue(buf, depth, "String", string);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        toStringBuilder(buf, 0);
        return buf.toString();
    }
}
