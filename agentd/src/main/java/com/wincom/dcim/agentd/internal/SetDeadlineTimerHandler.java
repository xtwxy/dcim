package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.NetworkService;
import com.wincom.dcim.agentd.primitives.DeadlineTimeout;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SetDeadlineTimer;
import io.netty.channel.Channel;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class SetDeadlineTimerHandler implements Handler {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private final Channel channel;
    private final NetworkService service;

    public SetDeadlineTimerHandler(Channel channel, NetworkService service) {
        this.channel = channel;
        this.service = service;
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {
        if (m instanceof SetDeadlineTimer) {
            SetDeadlineTimer setTimer = (SetDeadlineTimer) m;
            TimerTask tt = new TimerTask() {
                @Override
                public void run(Timeout tmt) throws Exception {
                    ctx.fire(new DeadlineTimeout());
                }

            };
            long deadline = setTimer.getTime().getTime();
            long now = System.currentTimeMillis();
            if (deadline > now) {
                service.getTimer().newTimeout(tt, (deadline - now), TimeUnit.MILLISECONDS);
            } else {
                ctx.fire(new DeadlineTimeout());
            }
        } else {
            log.warn("unknown message: " + m);
        }
        ctx.onSendComplete(m);
    }

}
