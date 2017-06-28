package com.wincom.dcim.agentd.messages;

import com.wincom.dcim.agentd.HandlerContext;

/**
 * Created by master on 6/21/17.
 */
public enum MessageType {
    UNKNOWN(-1) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    ACCEPT(0) {
        @Override
        public Message create(Object... params) {
            return new Accept((HandlerContext) params[0], (String) params[1], (Integer) params[2]);
        }
    },
    ACCEPTED(1) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    ACCEPT_FAILED(2) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    APPLICATION_FAILURE(3) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    BYTES_RECEIVED(4) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    CHANNEL_ACTIVE(5) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    CHANNEL_TIMEOUT(6) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    CLOSE_CONNECTION(7) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    CONNECT(8) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    CONNECTED(9) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    CONNECT_FAILED(10) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    CONNECTION_CLOSED(11) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    DEADLINE_TIMEOUT(12) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    EXECUTE_RUNNABLE(13) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    MILLSEC_FROM_NOW_TIMEOUT(14) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    PERIODIC_TIMEOUT(15) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    PUSH_EVENTS(16) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    READ_TIMEOUT(17) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    SEND_BYTES(18) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    SET_DEADLINE_TIMER(19) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    SET_MILLSEC_FROM_NOW_TIMER(20) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    SET_PERIODIC_TIMER(21) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    SYSTEM_ERROR(22) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    WRITE_COMPLETE(23) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    },
    WRITE_TIMEOUT(24) {
        @Override
        public Message create(Object... params) {
            return null;
        }
    };

    MessageType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public MessageType from(int code) {
        for (MessageType t : MessageType.class.getEnumConstants()) {
            if (t.code == code) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown request code 0x" + Integer.toHexString(code));
    }

    abstract public Message create(Object... params);

    private final int code;
}
