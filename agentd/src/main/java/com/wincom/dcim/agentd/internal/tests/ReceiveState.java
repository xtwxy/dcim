package com.wincom.dcim.agentd.internal.tests;

import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.CloseConnection;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SendBytes;
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
    byte[] ba = new byte[4096];

    public ReceiveState() {
        for (int i = 0; i < ba.length; ++i) {
            ba[i] = (byte) (0xff & (i % 10 + '0'));
        }
    }

    @Override
    public State on(HandlerContext ctx, Message m) {
        if (m instanceof BytesReceived) {
            // echo back the bytes.
            //ctx.send(new SendBytes(((BytesReceived) m).getByteBuffer()));
            return this;
        } else if (m instanceof Timeout) {
            sendBytes(ctx);

            return this;
        } else if (m instanceof WriteComplete) {
            sendBytes(ctx);
            ctx.onSendComplete();
            return this;
        } else {
            log.warn("unknown message: " + m);
            ctx.send(new CloseConnection());
            ctx.onSendComplete();
            return fail();
        }
    }

    private void sendBytes(HandlerContext ctx) {
        ByteBuffer buffer = ByteBuffer.wrap(ba);
        ctx.send(new SendBytes(buffer));
    }
}
