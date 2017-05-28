package com.wincom.dcim.agentd.internal.mocks;

import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SetDeadlineTimer;
import com.wincom.dcim.agentd.primitives.SetMillsecFromNowTimer;
import com.wincom.dcim.agentd.primitives.SetPeriodicTimer;
import com.wincom.dcim.agentd.primitives.TimerHandler;

/**
 *
 * @author master
 */
public class TimerHandlerImpl implements TimerHandler {

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
