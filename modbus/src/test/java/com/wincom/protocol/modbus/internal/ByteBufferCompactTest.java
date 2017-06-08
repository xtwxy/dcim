package com.wincom.protocol.modbus.internal;

import java.nio.ByteBuffer;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author master
 */
public class ByteBufferCompactTest {

    @Test
    public void testCompact() {
        byte[] bytes = new byte[]{0x00, 0x01, 0x02, 0x03};
        ByteBuffer buf = ByteBuffer.allocate(bytes.length);
        buf.put(bytes);
        buf.flip();

        assertEquals(0, buf.position());
        assertEquals(bytes.length, buf.remaining());
        assertEquals(bytes.length, buf.capacity());

        buf.get();
        buf.compact();
        buf.flip();

        assertEquals(0, buf.position());
        assertEquals(bytes.length - 1, buf.remaining());
        assertEquals(bytes.length, buf.capacity());
        
        buf.compact();
        buf.put((byte)0xff);
        buf.flip();
        
        assertEquals(0, buf.position());
        assertEquals(bytes.length, buf.remaining());
        assertEquals(bytes.length, buf.capacity());
        
        buf.position(1);
        buf.compact();
        buf.put((byte)0xff);
        buf.flip();
        
        assertEquals(0, buf.position());
        assertEquals(bytes.length, buf.remaining());
        assertEquals(bytes.length, buf.capacity());
    }
}
