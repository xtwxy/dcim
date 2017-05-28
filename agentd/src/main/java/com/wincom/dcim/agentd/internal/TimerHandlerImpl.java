package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.primitives.DeadlineTimeout;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.MillsecFromNowTimeout;
import com.wincom.dcim.agentd.primitives.SetDeadlineTimer;
import com.wincom.dcim.agentd.primitives.SetMillsecFromNowTimer;
import com.wincom.dcim.agentd.primitives.SetPeriodicTimer;
import com.wincom.dcim.agentd.primitives.TimerHandler;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public final class TimerHandlerImpl implements TimerHandler {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private final NetworkService service;

    TimerHandlerImpl(NetworkService service) {
        this.service = service;
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        log.info(String.format("handle(%s, %s, %s)", this, ctx, m));
    }

    @Override
    public void handleSetDeadlineTimer(HandlerContext ctx, SetDeadlineTimer m) {
        TimerTask tt = new TimerTask() {
            @Override
            public void run(Timeout tmt) throws Exception {
                ctx.fire(new DeadlineTimeout());
            }

        };
        long deadline = m.getTime().getTime();
        long now = System.currentTimeMillis();
        if (deadline > now) {
            service.getTimer().newTimeout(tt, (deadline - now), TimeUnit.MILLISECONDS);
        } else {
            ctx.fire(new DeadlineTimeout());
        }
    }

    @Override
    public void handleSetMillsecFromNowTimer(HandlerContext ctx, SetMillsecFromNowTimer m) {
        SetMillsecFromNowTimer setTimer = (SetMillsecFromNowTimer) m;
        TimerTask tt = new TimerTask() {
            @Override
            public void run(Timeout tmt) throws Exception {
                log.info("timout.");
                tmt.cancel();
                ctx.fire(new MillsecFromNowTimeout());
            }

        };
        Timeout timeout = service.getTimer().newTimeout(tt, setTimer.getMillsec(), TimeUnit.MILLISECONDS);
        ctx.set("timeout", timeout);
    }

    @Override
    public void handleSetPeriodicTimer(HandlerContext ctx, SetPeriodicTimer m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return "TimerHandlerImpl@" + this.hashCode();
    }
}
