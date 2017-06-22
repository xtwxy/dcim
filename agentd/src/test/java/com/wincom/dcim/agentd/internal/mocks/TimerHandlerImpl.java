package com.wincom.dcim.agentd.internal.mocks;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.messages.SetDeadlineTimer;
import com.wincom.dcim.agentd.messages.SetMillsecFromNowTimer;
import com.wincom.dcim.agentd.messages.SetPeriodicTimer;
import com.wincom.dcim.agentd.TimerOutboundHandler;

/**
 *
 * @author master
 */
public class TimerHandlerImpl implements TimerOutboundHandler {

    @Override
    public void handleSetDeadlineTimer(HandlerContext ctx, SetDeadlineTimer m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleSetMillsecFromNowTimer(HandlerContext ctx, SetMillsecFromNowTimer m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleSetPeriodicTimer(HandlerContext ctx, SetPeriodicTimer m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
