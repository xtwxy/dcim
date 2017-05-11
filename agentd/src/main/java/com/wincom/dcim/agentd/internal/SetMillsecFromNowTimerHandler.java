package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.MillsecFromNowTimeout;
import com.wincom.dcim.agentd.primitives.SetMillsecFromNowTimer;
import io.netty.channel.Channel;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class SetMillsecFromNowTimerHandler implements Handler {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private final Channel channel;
    private final NetworkService service;

    public SetMillsecFromNowTimerHandler(Channel channel, NetworkService service) {
        this.channel = channel;
        this.service = service;
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        if (m instanceof SetMillsecFromNowTimer) {
            SetMillsecFromNowTimer setTimer = (SetMillsecFromNowTimer) m;
            TimerTask tt = new TimerTask() {
                @Override
                public void run(Timeout tmt) throws Exception {
                    log.info("timout.");
                    ctx.fire(new MillsecFromNowTimeout());
                }

            };
            service.getTimer().newTimeout(tt, setTimer.getMillsec(), TimeUnit.MILLISECONDS);
        } else {
            log.warn("unknown message: " + m);
        }
        ctx.onSendComplete(m);
    }

}
