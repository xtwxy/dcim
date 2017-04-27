package com.wincom.protocol.modbus.internal;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import org.junit.Test;

/**
 *
 * @author master
 */
public class DatagramPacketTest {

    @Test
    public void testSend() throws Exception {
        DatagramSocket socket = new DatagramSocket();
        byte[] ctrlData = new byte[]{0x00, 0x01};
        DatagramPacket ctrlPack = new DatagramPacket(
                ctrlData, 
                ctrlData.length, 
                InetAddress.getByName("192.168.0.78"), 
                20006
        );
        socket.send(ctrlPack);
        socket.close();
    }
}
