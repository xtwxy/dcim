package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.domain.Signal;
import java.util.HashMap;
import java.util.Map;

public class SetSignalValues {

    public static class Request implements Message {

        private Map<String, Signal> values;

        public Request() {
            this.values = new HashMap<>();
        }

        public Request(Map<String, Signal> values) {
            this.values = values;
        }

        public Map<String, Signal> getValues() {
            return values;
        }

        public void setValues(Map<String, Signal> values) {
            this.values = values;
        }
        
        @Override
        public void apply(Handler handler) {
            handler.apply(this);
        }
    }
    
    public static class Response implements Message {

        private Map<String, ResultCode> results;

        public Response() {
            this.results = new HashMap<>();
        }

        public Response(Map<String, ResultCode> results) {
            this.results = results;
        }

        public Map<String, ResultCode> getResults() {
            return results;
        }

        public void setResults(Map<String, ResultCode> results) {
            this.results = results;
        }
        
        @Override
        public void apply(Handler handler) {
            handler.apply(this);
        }
    }
}
