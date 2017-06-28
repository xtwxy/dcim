package com.wincom.protocol.modbus;

import com.google.common.primitives.UnsignedBytes;
import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public enum ModbusFunction {
    READ_COILS((byte) 0x01) {
        @Override
        public ModbusPayload createRequest(HandlerContext sender) {
            throw new UnsupportedOperationException("Not supported yet."); 
        }

        @Override
        public ModbusPayload createResponse(HandlerContext sender) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    },
    READ_DISCRETE_INPUTS((byte) 0x02) {
        @Override
        public ModbusPayload createRequest(HandlerContext sender) {
            throw new UnsupportedOperationException("Not supported yet."); 
        }

        @Override
        public ModbusPayload createResponse(HandlerContext sender) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    },
    READ_MULTIPLE_HOLDING_REGISTERS((byte) 0x03) {
        @Override
        public ModbusPayload createRequest(HandlerContext sender) {
            return new ReadMultipleHoldingRegistersRequest(sender); 
        }
        

        @Override
        public ModbusPayload createResponse(HandlerContext sender) {
            return new ReadMultipleHoldingRegistersResponse(sender); 
        }
    },
    READ_INPUT_REGISTERS((byte) 0x04) {
        @Override
        public ModbusPayload createRequest(HandlerContext sender) {
            throw new UnsupportedOperationException("Not supported yet."); 
        }

        @Override
        public ModbusPayload createResponse(HandlerContext sender) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    },
    WRITE_SINGLE_COIL((byte) 0x05) {
        @Override
        public ModbusPayload createRequest(HandlerContext sender) {
            throw new UnsupportedOperationException("Not supported yet."); 
        }

        @Override
        public ModbusPayload createResponse(HandlerContext sender) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    },
    WRITE_SINGLE_HOLDING_REGISTER((byte) 0x06) {
        @Override
        public ModbusPayload createRequest(HandlerContext sender) {
            return new WriteSingleHoldingRegisterRequest(sender); 
        }

        @Override
        public ModbusPayload createResponse(HandlerContext sender) {
            return new WriteSingleHoldingRegisterResponse(sender);
        }
    },
    WRITE_MULTIPLE_HOLDING_COILS((byte) 0x0f) {
        @Override
        public ModbusPayload createRequest(HandlerContext sender) {
            return new WriteMultipleHoldingRegistersRequest(sender); 
        }

        @Override
        public ModbusPayload createResponse(HandlerContext sender) {
            return new WriteMultipleHoldingRegistersResponse(sender);
        }
    },
    WRITE_MULTIPLE_HOLDING_REGISTERS((byte) 0x10) {
        @Override
        public ModbusPayload createRequest(HandlerContext sender) {
            return new WriteSingleHoldingRegisterRequest(sender);
        }

        @Override
        public ModbusPayload createResponse(HandlerContext sender) {
            return new WriteSingleHoldingRegisterResponse(sender);
        }
    };

    ModbusFunction(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public static ModbusFunction from(byte b) {
        for(ModbusFunction c : ModbusFunction.class.getEnumConstants()) {
            if(c.getCode() == b) {
                return c;
            }
        }
        throw new IllegalArgumentException("Unknown modbus function code 0x" + UnsignedBytes.toString(b, 16));
    }
    public abstract ModbusPayload createRequest(HandlerContext sender);
    public abstract ModbusPayload createResponse(HandlerContext sender);
    private final byte code;
}
