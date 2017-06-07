package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.domain.Signal;
import java.util.HashMap;
import java.util.Map;

public final class SetSignalValues {

    public static class Request  extends Message.Adapter {

        private Map<String, Signal> values;

        public Request(HandlerContext sender) {
            super(sender);
            this.values = new HashMap<>();
        }

        public Request(HandlerContext sender, Map<String, Signal> values) {
            super(sender);
            this.values = values;
        }

        public Map<String, Signal> getValues() {
            return values;
        }

        public void setValues(Map<String, Signal> values) {
            this.values = values;
        }
    }
    
    public static class Response extends Message.Adapter {

        private Map<String, ResultCode> results;

        public Response(HandlerContext sender) {
            super(sender);
            this.results = new HashMap<>();
        }

        public Response(HandlerContext sender, Map<String, ResultCode> results) {
            super(sender);
            this.results = results;
        }

        public Map<String, ResultCode> getResults() {
            return results;
        }

        public void setResults(Map<String, ResultCode> results) {
            this.results = results;
        }
    }
}
