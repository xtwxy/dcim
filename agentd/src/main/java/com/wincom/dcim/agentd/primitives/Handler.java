package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.HandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public interface Handler {

    public void handle(HandlerContext ctx, Message m);

    public static class Default implements Handler {

        protected Logger log = LoggerFactory.getLogger(this.getClass());

        @Override
        public void handle(HandlerContext ctx, Message m) {
            log.info(String.format("handle(%s, %s)", ctx, m));
        }
    }
}
