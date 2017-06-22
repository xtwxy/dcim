package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.ChannelOutboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.domain.Signal;
import com.wincom.dcim.agentd.messages.ChannelInbound;
import com.wincom.dcim.agentd.messages.ChannelOutbound;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author master
 */
public final class GetSignalValues {

    public static class Request extends RequestMessage {

        private Set<String> keys;

        public Request(HandlerContext sender) {
            super(sender, PrimitiveMessageType.GET_SIGNAL_VALUES_REQUEST);
            this.keys = new HashSet<>();
        }

        public Request(HandlerContext sender, Set<String> keys) {
            super(sender, PrimitiveMessageType.GET_SIGNAL_VALUES_REQUEST);
            this.keys = keys;
        }

        public Set<String> getKeys() {
            return keys;
        }

        public void setKeys(Set<String> keys) {
            this.keys = keys;
        }

        @Override
        public void applyChannelOutbound(HandlerContext ctx, ChannelOutboundHandler handler) {
            handler.handleSendPayload(ctx, this);
        }
    }

    public static class Response extends ResponseMessage {

        private Map<String, Signal> values;

        public Response(HandlerContext sender) {
            super(sender, PrimitiveMessageType.GET_SIGNAL_VALUES_RESPONSE);
            this.values = new HashMap<>();
        }

        public Response(HandlerContext sender, HashMap<String, Signal> values) {
            super(sender, PrimitiveMessageType.GET_SIGNAL_VALUES_RESPONSE);
            this.values = values;
        }

        public Map<String, Signal> getValues() {
            return values;
        }

        public void setValues(Map<String, Signal> values) {
            this.values = values;
        }

        @Override
        public void applyChannelInbound(HandlerContext ctx, ChannelInboundHandler handler) {
            handler.handlePayloadReceived(ctx, this);
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, Signal> e : values.entrySet()) {
                sb.append("\t\t(");
                sb.append(e.getKey().toString()).append(", ").append(e.getValue().toString());
                sb.append(")\n");
            }
            return String.format("%s \n\tsender: %s, \n\tsignals: \n%s", getClass().getSimpleName(), getSender(), sb.toString());
        }
    }
}
