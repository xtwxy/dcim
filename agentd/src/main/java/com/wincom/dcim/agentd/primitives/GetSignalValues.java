package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.domain.Signal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author master
 */
public class GetSignalValues {

    public static class Request implements Message {

        private Set<String> keys;

        public Request() {
            this.keys = new HashSet<>();
        }

        public Request(Set<String> keys) {
            this.keys = keys;
        }

        public Set<String> getKeys() {
            return keys;
        }

        public void setKeys(Set<String> keys) {
            this.keys = keys;
        }
        
        @Override
        public void apply(Handler handler) {
            handler.handleGetSignalValuesRequest(this);
        }
    }
    
    public static class Response implements Message {
        private Map<String, Signal> values;
        
        public Response() {
            this.values = new HashMap<>();
        }
        public Response(HashMap<String, Signal> values) {
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
            handler.handleGetSignalValuesRequest(this);
        }
    }
}
