package com.wincom.protocol.modbus.internal.master;

import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.ChannelActive;
import com.wincom.dcim.agentd.messages.ChannelInactive;
import com.wincom.dcim.agentd.messages.ChannelTimeout;
import com.wincom.dcim.agentd.messages.ApplicationFailure;
import com.wincom.dcim.agentd.messages.BytesReceived;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.messages.SystemError;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 *
 * @author master
 */
public class MasterDecodeInboundHandlerImpl
        extends ChannelInboundHandler.Adapter {

    private ByteBuffer readBuffer;
    private final Map<Byte, MasterContextImpl> inboundContexts;

    public MasterDecodeInboundHandlerImpl(Map<Byte, MasterContextImpl> inboundContexts) {
        this.inboundContexts = inboundContexts;
    }

    @Override
    public void handleChannelActive(HandlerContext ctx, ChannelActive m) {
        super.handleChannelActive(ctx, m);
        readBuffer = (ByteBuffer) ctx.getOrSetIfNotExist(MasterCodecImpl.READ_BUFFER_KEY, ByteBuffer.allocate(2048));
        fireToInbounds(new ChannelActive(ctx));
    }

    @Override
    public void handleChannelInactive(HandlerContext ctx, ChannelInactive m) {
        ctx.onClosed(m);
        fireToInbounds(new ChannelInactive(ctx));
    }

    @Override
    public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m) {
        if(ctx.state().stopped()) {
            fireToInbounds(new ChannelTimeout(ctx));
        } else {
            ctx.state().on(ctx, m);
        }
    }

    @Override
    public void handlePayloadReceived(HandlerContext ctx, Message m) {
        BytesReceived b = (BytesReceived) m;
        readBuffer.put(b.getByteBuffer());
        readBuffer.flip();
        ctx.state().on(ctx, m);
    }

    @Override
    public void handlePayloadSent(HandlerContext ctx, Message m) {
        ctx.state().on(ctx, m);
    }

    @Override
    public void handleSystemError(HandlerContext ctx, SystemError m) {
        super.handleSystemError(ctx, m);
        ctx.state().on(ctx, m);
    }

    @Override
    public void handleApplicationFailure(HandlerContext ctx, ApplicationFailure m) {
        super.handleApplicationFailure(ctx, m);
        ctx.state().on(ctx, m);
    }

    private void fireToInbounds(Message m) {
        for (Map.Entry<Byte, MasterContextImpl> e : inboundContexts.entrySet()) {
            e.getValue().fire(m);
        }
    }
}
