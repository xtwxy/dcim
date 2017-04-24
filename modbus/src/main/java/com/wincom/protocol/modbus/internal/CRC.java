package com.wincom.protocol.modbus.internal;

/**
 *
 * @author master
 */
public class CRC {

    public static short crc16(byte[] b, int offset, int length) {
        if (b.length < length) {
            throw new IllegalArgumentException("buffer length less than packet length");
        }
        
        int crc = 0xFFFF;
        for (int i = offset; i < offset + length; i++) {
            crc = (crc ^ (0xff & b[i])) & 0xffff;
            for (int j = 0; j < 8; j++) {
                if ((crc & 0x0001) != 0) {
                    crc = ((crc >> 1) ^ 0xA001) & 0xffff;
                } else {
                    crc = (crc >> 1);
                }
            }
        }
        crc = ((crc >> 8) & 0xff) | ((crc << 8) & 0xff00);
        System.out.printf("crc16: 0x%04x\n", 0xffff & crc);
        return (short) (0xffff & crc);
    }
}
