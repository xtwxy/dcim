package com.wincom.driver.dds3366d.internal.primitives;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.protocol.modbus.*;
import java.nio.ByteBuffer;

/**
 *
 * @author master
 */
public class ReadStatus {

    public static class Request extends AbstractWireable implements Message {

        @Override
        public int getWireLength() {
            throw new UnsupportedOperationException("Not supported yet."); 
        }

        @Override
        public void toWire(ByteBuffer buffer) {
            throw new UnsupportedOperationException("Not supported yet."); 
        }

        @Override
        public void fromWire(ByteBuffer buffer) {
            throw new UnsupportedOperationException("Not supported yet."); 
        }

        @Override
        public void apply(Handler handler) {
            throw new UnsupportedOperationException("Not supported yet."); 
        }

    }
}
