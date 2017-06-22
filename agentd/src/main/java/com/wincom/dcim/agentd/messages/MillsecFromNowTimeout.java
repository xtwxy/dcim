package com.wincom.dcim.agentd.messages;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.TimerInboundHandler;

/**
 *
 * @author master
 */
public class MillsecFromNowTimeout extends Timeout {

    public MillsecFromNowTimeout(HandlerContext sender) {
        super(sender);
    }

    @Override
    public void applyTimout(HandlerContext ctx, TimerInboundHandler handler) {
        handler.handleMillsecFromNowTimeout(ctx, this);
    }
}
