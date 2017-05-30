package com.wincom.dcim.agentd.internal.mocks;

import com.wincom.dcim.agentd.primitives.Accept;
import com.wincom.dcim.agentd.primitives.ChannelOutboundHandler;
import com.wincom.dcim.agentd.primitives.CloseConnection;
import com.wincom.dcim.agentd.primitives.Connect;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;

/**
 *
 * @author master
 */
public class OutboundHandlerImpl implements ChannelOutboundHandler {

    private ChannelOutboundHandler delegate;

    public void setDelegate(ChannelOutboundHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public void handleAccept(HandlerContext ctx, Accept m) {
        delegate.handleAccept(ctx, m);
    }

    @Override
    public void handleConnect(HandlerContext ctx, Connect m) {
        delegate.handleConnect(ctx, m);
    }

    @Override
    public void handleClose(HandlerContext ctx, CloseConnection m) {
        delegate.handleClose(ctx, m);
    }

    @Override
    public void handleSendPayload(HandlerContext ctx, Message m) {
        delegate.handleSendPayload(ctx, m);
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        delegate.handle(ctx, m);
    }

}
