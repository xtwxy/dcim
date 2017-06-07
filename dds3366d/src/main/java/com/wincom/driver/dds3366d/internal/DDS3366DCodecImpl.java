package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.AgentdService;
import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.HandlerContext.DisposeHandler;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author master
 */
public class DDS3366DCodecImpl implements Codec {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final DDS3366CodecHandleContext decodeContext;

    public DDS3366DCodecImpl(final HandlerContext outboundHandlerContext) {
        decodeContext = new DDS3366CodecHandleContext();
        outboundHandlerContext.addInboundContext(decodeContext);
        decodeContext.addDisposeHandler(new DisposeHandler() {
            @Override
            public void onDispose(HandlerContext ctx) {
                outboundHandlerContext.removeInboundContext(ctx);
            }
        });
    }

    @Override
    public HandlerContext openInbound(AgentdService service, Properties props) {
        return decodeContext;
    }

    public HandlerContext getCodecContext() {
        return decodeContext;
    }
}
