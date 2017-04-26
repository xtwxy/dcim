package com.wincom.protocol.modbus.internal;

import com.wincom.protocol.modbus.ModbusFrame;
import com.wincom.protocol.modbus.ReadMultipleHoldingRegistersRequest;
import java.nio.ByteBuffer;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 *
 * @author master
 */
public class ReadMultipleHoldingRegistersRequestTest {

    @Test
    public void testMarshal1() {
        byte[] b = new byte[]{
            0x01, 0x03, 0x09, 0x24, 0x00, 0x0E, (byte) 0x87, (byte) 0x99
        };

        ModbusFrame frame = new ModbusFrame();
        ReadMultipleHoldingRegistersRequest request = new ReadMultipleHoldingRegistersRequest();
        frame.setSlaveAddress((byte) 0x01);
        frame.setPayload(request);
        request.setStartAddress((short) 0x0924);
        request.setNumberOfRegisters((short) 0x000e);

        ByteBuffer buf = ByteBuffer.allocate(frame.getWireLength());
        frame.toWire(buf);

        assertArrayEquals(b, buf.array());

        System.out.println(request);
    }

    @Test
    public void testMarshal2() {
        byte[] b = new byte[]{
            0x01, 0x03, 0x00, 0x32, 0x00, 0x03, (byte) 0xa4, (byte) 0x04
        };

        ModbusFrame frame = new ModbusFrame();
        ReadMultipleHoldingRegistersRequest request = new ReadMultipleHoldingRegistersRequest();
        frame.setSlaveAddress((byte) 0x01);
        frame.setPayload(request);
        request.setStartAddress((short) 0x0032);
        request.setNumberOfRegisters((short) 0x0003);

        ByteBuffer buf = ByteBuffer.allocate(frame.getWireLength());
        frame.toWire(buf);

        assertArrayEquals(b, buf.array());

        System.out.println(request);
    }
}
