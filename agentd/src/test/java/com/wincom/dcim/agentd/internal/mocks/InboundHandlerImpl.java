package com.wincom.dcim.agentd.internal.mocks;

import com.wincom.dcim.agentd.messages.ChannelActive;
import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.messages.ChannelTimeout;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.messages.SendBytes;
import java.nio.ByteBuffer;

/**
 *
 * @author master
 */
public class InboundHandlerImpl extends ChannelInboundHandler.Adapter {

    @Override
    public void handleChannelActive(HandlerContext ctx, ChannelActive m) {
        log.info(String.format("handleChannelActive(%s, %s)", ctx, m));
        ctx.getOutboundHandler().setOutboundContext(m.getSender());
        ctx.setActive(true);

        ctx.fireInboundHandlerContexts(new ChannelActive(ctx));

        ByteBuffer buffer = ByteBuffer.wrap("Hello, World!".getBytes());
        Message sendBytes = new SendBytes(ctx, buffer);

        ctx.send(sendBytes);
    }

    @Override
    public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m) {
        ctx.onRequestCompleted();
    }

    @Override
    public void handlePayloadReceived(HandlerContext ctx, Message m) {
        ctx.onRequestCompleted();
        sendBytes(ctx);
    }

    private static final byte[] BA = new byte[128];

    static {
        for (int i = 0; i < BA.length; ++i) {
            BA[i] = (byte) (0xff & (i % 10 + '0'));
        }
    }

    private void sendBytes(HandlerContext ctx) {
        ByteBuffer buffer = ByteBuffer.wrap(BA);
        ctx.send(new SendBytes(ctx, buffer));
    }
}
