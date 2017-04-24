package com.wincom.protocol.modbus.internal;

import java.nio.ByteBuffer;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author master
 */
public class CRCTest {

    @Test
    public void testCrc16() {
        //01 03 09 24 00 0E 87 99
        byte[] b = new byte[]{0x01, 0x03, 0x09, 0x24, 0x00, 0x0E, (byte) 0x87, (byte) 0x99};

        ByteBuffer buf = ByteBuffer.allocate(b.length);

        buf.put(b);

        short crc16 = (short) ((0xff00 & ((0xff & b[b.length - 2]) << 8))
                | (0xff & b[b.length - 1]));
        short calculated = CRC.crc16(buf.array(), 0, buf.capacity() - 2);

        assertEquals(crc16, calculated);
    }
}
