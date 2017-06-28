package com.wincom.driver.dds3366d.internal;

import java.util.Properties;

import com.wincom.dcim.agentd.Codec;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.HandlerContext.DisposeHandler;

/**
 *
 * @author master
 */
public class DDS3366DCodecImpl implements Codec {

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
    public HandlerContext openInbound(Properties props) {
        return decodeContext;
    }

    public HandlerContext getCodecContext() {
        return decodeContext;
    }
}
