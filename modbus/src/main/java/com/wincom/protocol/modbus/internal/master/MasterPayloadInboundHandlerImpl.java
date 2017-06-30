package com.wincom.protocol.modbus.internal.master;

import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.ChannelActive;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.protocol.modbus.ModbusPayloadInboundHandler;
import com.wincom.protocol.modbus.ReadMultipleHoldingRegistersResponse;
import com.wincom.protocol.modbus.WriteMultipleHoldingRegistersResponse;
import com.wincom.protocol.modbus.WriteSingleHoldingRegisterResponse;

/**
 *
 * @author master
 */
public class MasterPayloadInboundHandlerImpl
        extends ChannelInboundHandler.Adapter
        implements ModbusPayloadInboundHandler {

    @Override
    public void handleChannelActive(HandlerContext ctx, ChannelActive m) {
        log.info(String.format("handleChannelActive(%s, %s)", ctx, m));
        ctx.getOutboundHandler().setOutboundContext(m.getSender());
        ctx.setActive(true);

        ctx.fireInboundHandlerContexts(new ChannelActive(ctx));
    }

    @Override
    public void handlePayloadReceived(HandlerContext ctx, Message m) {
        ctx.fireInboundHandlerContexts(m);
        ctx.onRequestCompleted();
    }

    @Override
    public void handlePayloadSent(HandlerContext ctx, Message m) {
    }

    @Override
    public void handleReadMultipleHoldingRegistersResponse(HandlerContext ctx, ReadMultipleHoldingRegistersResponse m) {
        handlePayloadReceived(ctx, m);
    }

    @Override
    public void handleWriteMultipleHoldingRegistersResponse(HandlerContext ctx, WriteMultipleHoldingRegistersResponse m) {
        handlePayloadReceived(ctx, m);
    }

    @Override
    public void handleWriteSingleHoldingRegisterResponse(HandlerContext ctx, WriteSingleHoldingRegisterResponse m) {
        handlePayloadReceived(ctx, m);
    }
}
