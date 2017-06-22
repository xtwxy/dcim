package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.ChannelInboundHandler;
import com.wincom.dcim.agentd.ChannelOutboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.domain.Signal;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.messages.ResultCode;

import java.util.HashMap;
import java.util.Map;

public final class SetSignalValues {

    public static class Request extends RequestMessage {

        private Map<String, Signal> values;

        public Request(HandlerContext sender) {
            super(sender, PrimitiveMessageType.SET_SIGNAL_VALUES_REQUEST);
            this.values = new HashMap<>();
        }

        public Request(HandlerContext sender, Map<String, Signal> values) {
            super(sender, PrimitiveMessageType.SET_SIGNAL_VALUES_REQUEST);
            this.values = values;
        }

        @Override
        public void applyChannelOutbound(HandlerContext ctx, ChannelOutboundHandler handler) {
            handler.handleSendPayload(ctx, this);
        }

        public Map<String, Signal> getValues() {
            return values;
        }

        public void setValues(Map<String, Signal> values) {
            this.values = values;
        }

        @Override
        public String toString() {
            return String.format("%s %s %s", getClass().getSimpleName(), getSender(), values);
        }
    }

    public static class Response extends ResponseMessage {

        private Map<String, ResultCode> results;

        public Response(HandlerContext sender) {
            super(sender, PrimitiveMessageType.SET_SIGNAL_VALUES_RESPONSE);
            this.results = new HashMap<>();
        }

        public Response(HandlerContext sender, Map<String, ResultCode> results) {
            super(sender, PrimitiveMessageType.SET_SIGNAL_VALUES_RESPONSE);
            this.results = results;
        }

        @Override
        public void applyChannelInbound(HandlerContext ctx, ChannelInboundHandler handler) {
            handler.handlePayloadReceived(ctx, this);
        }

        public Map<String, ResultCode> getResults() {
            return results;
        }

        public void setResults(Map<String, ResultCode> results) {
            this.results = results;
        }
    }
}
