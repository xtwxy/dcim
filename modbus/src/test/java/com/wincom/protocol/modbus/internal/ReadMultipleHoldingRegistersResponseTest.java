package com.wincom.protocol.modbus.internal;

import com.wincom.protocol.modbus.ReadMultipleHoldingRegistersResponse;
import java.nio.ByteBuffer;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 *
 * @author master
 */
public class ReadMultipleHoldingRegistersResponseTest {

    @Test
    public void testMarshal1() {
        byte[] b = new byte[]{
            0x01, 0x03, 0x0C, 0x41, (byte) 0xC8, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x41, (byte) 0xEA, 0x66, 0x66, 0x27, 
            (byte) 0xAD
        };

        ReadMultipleHoldingRegistersResponse response = new ReadMultipleHoldingRegistersResponse();
        response.setSlaveAddress((byte) 0x01);
        response.setNumberOfBytes((byte) 0xc);
        byte[] bytes = new byte[0x0c];
        System.arraycopy(b, 3, bytes, 0, bytes.length);
        response.setBytes(bytes);

        ByteBuffer buf = ByteBuffer.allocate(response.getWireLength());
        response.toWire(buf);

        System.out.println(response);
        assertArrayEquals(b, buf.array());
    }
}
