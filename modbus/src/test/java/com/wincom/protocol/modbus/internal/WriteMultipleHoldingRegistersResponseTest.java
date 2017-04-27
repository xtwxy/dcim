package com.wincom.protocol.modbus.internal;

import com.wincom.protocol.modbus.ModbusFrame;
import com.wincom.protocol.modbus.WriteMultipleHoldingRegistersResponse;
import java.nio.ByteBuffer;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 *
 * @author master
 */
public class WriteMultipleHoldingRegistersResponseTest {

    byte[] b = new byte[]{
        0x01, 0x10, 0x00, 0x00, 0x00, 0x02, 0x41, (byte)0xc8
    };

    @Test
    public void testMarshal1() {
        ModbusFrame frame = new ModbusFrame();
        WriteMultipleHoldingRegistersResponse response = new WriteMultipleHoldingRegistersResponse();
        frame.setSlaveAddress((byte) 0x01);
        frame.setPayload(response);
        response.setStartAddress((short) 0x0000);
        response.setNumberOfRegisters((short) 0x0002);

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
        
        ModbusFrame frame = new ModbusFrame();

        frame.fromWire(buf);

        System.out.println(frame);
    }
}
