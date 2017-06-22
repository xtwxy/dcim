package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.domain.Signal;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.messages.ResultCode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by master on 6/21/17.
 */
public enum PrimitiveMessageType {
   GET_SIGNAL_VALUES_REQUEST(50) {
        @Override
        public Message create(Object... params) {
            if (params.length == 1) {
                return new GetSignalValues.Request((HandlerContext) params[0]);
            } else {
                return new GetSignalValues.Request((HandlerContext) params[0], (Set<String>) params[1]);
            }
        }
    },
    GET_SIGNAL_VALUES_RESPONSE(51) {
        @Override
        public Message create(Object... params) {
            if (params.length == 1) {
                return new GetSignalValues.Response((HandlerContext) params[0]);
            } else {
                return new GetSignalValues.Response((HandlerContext) params[0], (HashMap<String, Signal>) params[1]);
            }
        }
    },
    SET_SIGNAL_VALUES_REQUEST(52) {
        @Override
        public Message create(Object... params) {
            if (params.length == 1) {
                return new SetSignalValues.Response((HandlerContext) params[0]);
            } else {
                return new SetSignalValues.Response((HandlerContext) params[0], (Map<String, ResultCode>) params[1]);
            }
        }
    },
    SET_SIGNAL_VALUES_RESPONSE(53) {
        @Override
        public Message create(Object... params) {
            if (params.length == 1) {
                return new SetSignalValues.Response((HandlerContext) params[0]);
            } else {
                return new SetSignalValues.Response((HandlerContext) params[0], (Map<String, ResultCode>) params[1]);
            }
        }
    };

    PrimitiveMessageType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public PrimitiveMessageType from(int code) {
        for (PrimitiveMessageType t : PrimitiveMessageType.class.getEnumConstants()) {
            if (t.code == code) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown request code 0x" + Integer.toHexString(code));
    }

    abstract public Message create(Object... params);

    private int code;
}
