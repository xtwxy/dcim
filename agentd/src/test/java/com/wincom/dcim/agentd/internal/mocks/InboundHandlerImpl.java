package com.wincom.dcim.agentd.internal.mocks;

import com.wincom.dcim.agentd.primitives.Accepted;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelInboundHandler;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.Connected;
import com.wincom.dcim.agentd.primitives.ConnectionClosed;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SendBytes;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class InboundHandlerImpl implements ChannelInboundHandler {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handleAccepted(HandlerContext ctx, Accepted m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleConnected(HandlerContext ctx, Connected m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleChannelActive(HandlerContext ctx, ChannelActive m) {
        ByteBuffer buffer = ByteBuffer.wrap("Hello, World!".getBytes());
        Message sendBytes = new SendBytes(buffer);

        HandlerContext reply = new HandlerContext.NullContext();

        sendBytes.apply(reply, m.getContext().getOutboundHandler());
    }

    @Override
    public void handleChannelInactive(HandlerContext ctx, ChannelInactive m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m) {
        sendBytes(ctx);
    }

    @Override
    public void handleConnectionClosed(HandlerContext ctx, ConnectionClosed m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handlePayloadReceived(HandlerContext ctx, Message m) {
        log.info(String.format("handlePayloadReceived(%s, %s, %s)", this, ctx, m));
    }

    @Override
    public void handlePayloadSent(HandlerContext ctx, Message m) {
        log.info(String.format("handlePayloadSent(%s, %s, %s)", this, ctx, m));
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        log.info(String.format("handle(%s, %s, %s)", this, ctx, m));
    }

    private static final byte[] BA = new byte[128];

    static {
        for (int i = 0; i < BA.length; ++i) {
            BA[i] = (byte) (0xff & (i % 10 + '0'));
        }
    }

    private static void sendBytes(HandlerContext ctx) {
        ByteBuffer buffer = ByteBuffer.wrap(BA);
        ctx.send(new SendBytes(buffer));
    }
}
