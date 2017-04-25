package com.wincom.protocol.modbus.internal;

import com.wincom.protocol.modbus.WriteSingleHoldingRegisterResponse;
import java.nio.ByteBuffer;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 *
 * @author master
 */
public class WriteSingleHoldingRegisterResponseTest {

    @Test
    public void testMarshal1() {
        byte[] b = new byte[]{0x01, 0x06, 0x00, 0x02, 0x00, 0x02, (byte) 0xa9, (byte) 0xcb};

        WriteSingleHoldingRegisterResponse response = new WriteSingleHoldingRegisterResponse();
        response.setSlaveAddress((byte) 0x01);
        response.setStartAddress((short) 0x0002);
        response.setValueWritten((short) 0x0002);

        ByteBuffer buf = ByteBuffer.allocate(response.getWireLength());
        response.toWire(buf);

        assertArrayEquals(b, buf.array());

        System.out.println(response);
    }
}
