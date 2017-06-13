package com.wincom.dcim.integration.internal;

import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.ApplicationFailure;
import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.GetSignalValues;
import com.wincom.dcim.agentd.primitives.Message;
import java.util.Set;

/**
 *
 * @author master
 */
public class TestChannelInboundHandlerImpl extends ChannelInboundHandler.Adapter {

    @Override
    public void handleChannelActive(HandlerContext ctx, ChannelActive m) {
        log.info(String.format("handleChannelActive(%s, %s)", ctx, m));
        log.info(String.format("getOutboundHandler() = %s", ctx.getOutboundHandler()));
        super.handleChannelActive(ctx, m);
        sendRequest(ctx);
    }

    @Override
    public void handlePayloadReceived(HandlerContext ctx, Message m) {
        log.info(String.format("handlePayloadReceived(%s, %s)", ctx, m));
        sendRequest(ctx);
        ctx.onRequestCompleted();
    }

    @Override
    public void handleChannelTimeout(HandlerContext ctx, ChannelTimeout m) {
        log.info(String.format("handleChannelTimeout(%s, %s)", ctx, m));
        super.handleChannelTimeout(ctx, m);
        sendRequest(ctx);
    }

    @Override
    public void handleApplicationFailure(HandlerContext ctx, ApplicationFailure m) {
        ctx.onRequestCompleted();
    }

    private void sendRequest(HandlerContext ctx) {
        GetSignalValues.Request request = new GetSignalValues.Request(ctx);
        Set<String> keys = request.getKeys();
        keys.add("activePowerCombo");
        keys.add("positiveActivePower");
        keys.add("reverseActivePower");
        keys.add("voltage");
        keys.add("current");
        keys.add("power");
        keys.add("powerFactor");
        keys.add("frequency");
        keys.add("clock");
        keys.add("slaveAddress");
        keys.add("pt");
        keys.add("ct");
        
        ctx.send(request);
    }
}
