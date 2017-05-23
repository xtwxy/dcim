package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class DDS3366DCodecImpl implements Codec {

    Logger log = LoggerFactory.getLogger(this.getClass());


    public DDS3366DCodecImpl() {
    }


    @Override
    public void codecActive(HandlerContext outboundContext) {

    }

    @Override
    public HandlerContext openInbound(AgentdService service, Properties props, Handler inboundHandler) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handle(HandlerContext ctx, Message m) {

    }
}
