package com.wincom.dcim.agentd.internal.tests;

import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.CloseConnection;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.Timeout;
import com.wincom.dcim.agentd.primitives.WriteComplete;
import com.wincom.dcim.agentd.statemachine.State;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class ReceiveState extends State.Adapter {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public State on(HandlerContext ctx, Message m) {
        if (m instanceof BytesReceived) {
            ctx.send(m);
            BytesReceived br = (BytesReceived) m;
            ByteBuffer buffer = br.getByteBuffer();
            return this;
        } else if (m instanceof Timeout) {
            log.info("channelActive");
            byte[] ba = new byte[4096];
            for (int i = 0; i < ba.length; ++i) {
                ba[i] = (byte) (0xff & (i % 10 + '0'));
            }
            ByteBuffer buffer = ByteBuffer.wrap(ba);
            ctx.send(new BytesReceived(buffer));

            return this;
        } else if (m instanceof WriteComplete) {
            return this;
        } else {
            log.warn("unknown message: " + m);
            ctx.send(new CloseConnection());
            return fail();
        }
    }
}
