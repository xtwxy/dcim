package com.wincom.protocol.modbus.internal;

import com.wincom.protocol.modbus.WriteMultipleHoldingRegistersResponse;
import java.nio.ByteBuffer;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 *
 * @author master
 */
public class WriteMultipleHoldingRegistersResponseTest {

    @Test
    public void testMarshal1() {
        byte[] b = new byte[]{
            0x01, 0x10, 0x00, 0x00, 0x00, 0x02, 0x41, (byte)0xc8
        };

        WriteMultipleHoldingRegistersResponse response = new WriteMultipleHoldingRegistersResponse();
        response.setSlaveAddress((byte) 0x01);
        response.setStartAddress((short) 0x0000);
        response.setNumberOfRegisters((short) 0x0002);

        ByteBuffer buf = ByteBuffer.allocate(response.getWireLength());
        response.toWire(buf);

        assertArrayEquals(b, buf.array());

        System.out.println(response);
    }
}
