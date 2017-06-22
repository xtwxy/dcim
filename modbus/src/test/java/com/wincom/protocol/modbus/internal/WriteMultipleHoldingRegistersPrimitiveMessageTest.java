package com.wincom.protocol.modbus.internal;

import com.wincom.protocol.modbus.ModbusFrame;
import com.wincom.protocol.modbus.WriteMultipleHoldingRegistersRequest;
import java.nio.ByteBuffer;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 *
 * @author master
 */
public class WriteMultipleHoldingRegistersPrimitiveMessageTest {

    @Test
    public void testMarshal1() {
        byte[] b = new byte[]{
            0x01, 0x10, 0x00, 0x00, 0x00, 0x02, 0x04, 0x00,
            0x64, 0x00, 0x00, (byte)0xb2, 0x70
        };

        ModbusFrame frame = new ModbusFrame(null);
        WriteMultipleHoldingRegistersRequest request = new WriteMultipleHoldingRegistersRequest(null);
        frame.setSlaveAddress((byte) 0x01);
        frame.setPayload(request);
        request.setStartAddress((short) 0x00);
        request.setNumberOfRegisters((short) 0x0002);
        short[] registers = request.getRegisters();
        registers[0] = 0x64;
        registers[1] = 0x00;

        ByteBuffer buf = ByteBuffer.allocate(frame.getWireLength());
        frame.toWire(buf);

        assertArrayEquals(b, buf.array());

        System.out.println(request);
    }
}
