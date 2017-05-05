package com.wincom.driver.dds3366d.internal.primitives;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static java.lang.System.out;

/**
 * Created by master on 5/4/17.
 */
public class ReadSettingsResponseTest {
    @Test
    public void testMarshal0() throws ParseException {
        ReadSettings.Response response = new ReadSettings.Response();

        response.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2017-06-64 00:00:00"));
        response.setCt((short) 0xbabe);
        response.setPt((short) 0xcafe);
        response.setSlaveAddress((short) 0x64);

        ByteBuffer buffer = ByteBuffer.allocate(response.getWireLength());

        response.toWire(buffer);

        buffer.flip();

        out.printf("wire length: %d\n", buffer.remaining());
        for(byte b : buffer.array()) {
            out.printf("0x%02x ", 0xff & b);
        }
        out.println();


    }
}
