package com.wincom.dcim.agentd.primitives;

import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * An object which may be written to and read from a {@link ByteBuffer}.
 *
 * @author shevek
 */
public interface Wireable {

    /**
     * Returns the wire length of this packet.
     *
     * @return
     */
    @Nonnegative
    public int getWireLength();

    /**
     * Writes bytes from this object onto the wire, serializing it.
     *
     * @param buffer
     */
    public void toWire(@Nonnull ByteBuffer buffer);

    /**
     * Reads bytes from the wire into this object, de-serializing it.
     *
     * @param buffer
     */
    public void fromWire(@Nonnull ByteBuffer buffer);

    public void toStringBuilder(@Nonnull StringBuilder buf, @Nonnegative int depth);
}
