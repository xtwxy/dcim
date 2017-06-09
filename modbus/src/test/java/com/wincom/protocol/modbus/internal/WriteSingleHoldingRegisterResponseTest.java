package com.wincom.protocol.modbus.internal;

import com.wincom.protocol.modbus.ModbusFrame;
import com.wincom.protocol.modbus.WriteSingleHoldingRegisterResponse;
import java.nio.ByteBuffer;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 *
 * @author master
 */
public class WriteSingleHoldingRegisterResponseTest {

    byte[] b = new byte[]{
        0x01, 0x06, 0x00, 0x02, 0x00, 0x02, (byte) 0xa9, (byte) 0xcb
    };

    @Test
    public void testMarshal1() {

        ModbusFrame frame = new ModbusFrame(null, false);
        WriteSingleHoldingRegisterResponse response = new WriteSingleHoldingRegisterResponse(null);
        frame.setSlaveAddress((byte) 0x01);
        frame.setPayload(response);
        response.setStartAddress((short) 0x0002);
        response.setValueWritten((short) 0x0002);

        ByteBuffer buf = ByteBuffer.allocate(frame.getWireLength());
        frame.toWire(buf);

        assertArrayEquals(b, buf.array());

        System.out.println(response);
    }
    
    @Test
    public void testUnmarshal1() {
        ByteBuffer buf = ByteBuffer.allocate(b.length);
        buf.put(b);
        buf.flip();
        
        ModbusFrame frame = new ModbusFrame(null, false);

        frame.fromWire(buf);

        System.out.println(frame);
    }
}
