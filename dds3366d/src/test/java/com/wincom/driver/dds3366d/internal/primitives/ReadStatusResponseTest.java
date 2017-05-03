package com.wincom.driver.dds3366d.internal.primitives;

import static java.lang.System.out;
import java.nio.ByteBuffer;
import org.junit.Test;

/**
 *
 * @author master
 */
public class ReadStatusResponseTest {
    @Test
    public void testMarshal0() {
        ReadStatusResponse response = new ReadStatusResponse();
        
        response.setActivePowerCombo(300.99);
        response.setCurrent(2000.44);
        response.setFrequency(50.44);
        response.setPositiveActivePower(1000.00);
        response.setPower(1000.00);
        response.setPowerFactor(0.99);
        response.setReverseActivePower(10.0);
        ByteBuffer buf = ByteBuffer.allocate(response.getWireLength());
        response.toWire(buf);
        buf.flip();
        
        out.printf("wire length: %d\n", buf.remaining());
        for(byte b : buf.array()) {
            out.printf("0x%02x ", 0xff & b);
        }
        out.println();
    }
}
