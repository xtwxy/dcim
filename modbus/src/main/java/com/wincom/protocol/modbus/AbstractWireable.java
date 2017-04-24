package com.wincom.protocol.modbus;

import com.google.common.primitives.UnsignedBytes;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public abstract class AbstractWireable implements Wireable {

    @Nonnull
    protected static StringBuilder indent(@Nonnull StringBuilder buf, int depth) {
        for (int i = 0; i < depth; i++) {
            buf.append("  ");
        }
        return buf;
    }

    @Nonnull
    protected static StringBuilder appendHeader(@Nonnull StringBuilder buf, int depth, String name) {
        indent(buf, depth);
        buf.append(name).append(":").append('\n');
        return buf;
    }

    @Nonnull
    protected static StringBuilder appendValue(@Nonnull StringBuilder buf, int depth, String name, byte[] value) {
        indent(buf, depth);
        buf.append(name).append(": ");
        for(byte b : value) {
            buf.append(UnsignedBytes.toString(b, 16)).append(' ');
        }
        buf.append('\n');
        
        return buf;
    }

    @Nonnull
    protected static StringBuilder appendValue(@Nonnull StringBuilder buf, int depth, String name, Object value) {
        indent(buf, depth);
        buf.append(name).append(": ").append(value).append('\n');
        return buf;
    }

    @Nonnull
    protected static StringBuilder appendChild(@Nonnull StringBuilder buf, int depth, @Nonnull String name, @CheckForNull Wireable value) {
        if (value == null) {
            appendHeader(buf, depth, name);
            indent(buf, depth).append("null\n");
        } else {
            appendValue(buf, depth, name, value.getClass().getSimpleName());
            value.toStringBuilder(buf, depth + 1);
        }
        return buf;
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        indent(buf, depth).append(super.toString()).append('\n');
    }
    
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        toStringBuilder(buf, 0);
        return buf.toString();
    }
}
