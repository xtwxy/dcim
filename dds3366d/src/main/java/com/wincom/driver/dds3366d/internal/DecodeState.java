package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.primitives.ChannelActive;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.protocol.modbus.ModbusPayload;
import java.nio.ByteBuffer;

/**
 *
 * @author master
 */
public class DecodeState extends State.Adapter {

    private final ByteBuffer readBuffer;

    public DecodeState() {
        this.readBuffer = ByteBuffer.allocate(2048);
    }

    @Override
    public State on(HandlerContext ctx, Message m) {
        
        if (m instanceof ModbusPayload) {

            return success();
        } else if (m instanceof ChannelTimeout) {
            ctx.onSendComplete(m);
            return success();
        } else if (m instanceof ChannelActive) {
            ctx.setActive(true);
            ctx.getInboundHandler().handle(ctx, m);
            // TODO: notify the inbound handlers.
            return success();
        } else if (m instanceof ChannelInactive) {
            ctx.setActive(false);
            ctx.close();
            ctx.fireClosed(m);
            return fail();
        } else {
            log.info(String.format("default: (%s, %s, %s), leave to the inbound handler.", this, ctx, m));
            ctx.getInboundHandler().handle(ctx, m);
            return success();
        }
    }

    @Override
    public String toString() {
        return "DecodeState@" + this.hashCode();
    }
}
