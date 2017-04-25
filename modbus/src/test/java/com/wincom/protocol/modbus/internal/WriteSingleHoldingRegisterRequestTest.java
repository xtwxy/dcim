package com.wincom.protocol.modbus.internal;

import com.wincom.protocol.modbus.WriteSingleHoldingRegisterRequest;
import java.nio.ByteBuffer;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 *
 * @author master
 */
public class WriteSingleHoldingRegisterRequestTest {

    @Test
    public void testMarshal1() {
        byte[] b = new byte[]{0x01, 0x06, 0x00, 0x02, 0x00, 0x02, (byte) 0xa9, (byte) 0xcb};

        WriteSingleHoldingRegisterRequest request = new WriteSingleHoldingRegisterRequest();
        request.setSlaveAddress((byte) 0x01);
        request.setStartAddress((short) 0x0002);
        request.setValueToWrite((short) 0x0002);

        ByteBuffer buf = ByteBuffer.allocate(request.getWireLength());
        request.toWire(buf);

        assertArrayEquals(b, buf.array());

        System.out.println(request);
    }
}
