package com.wincom.protocol.modbus;

import com.google.common.primitives.UnsignedBytes;

/**
 *
 * @author master
 */
public enum ModbusFunction {
    READ_COILS((byte) 0x01) {
        @Override
        public ModbusPayload createRequest() {
            throw new UnsupportedOperationException("Not supported yet."); 
        }

        @Override
        public ModbusPayload createResponse() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    },
    READ_DISCRETE_INPUTS((byte) 0x02) {
        @Override
        public ModbusPayload createRequest() {
            throw new UnsupportedOperationException("Not supported yet."); 
        }

        @Override
        public ModbusPayload createResponse() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    },
    READ_MULTIPLE_HOLDING_REGISTERS((byte) 0x03) {
        @Override
        public ModbusPayload createRequest() {
            return new ReadMultipleHoldingRegistersRequest(); 
        }
        

        @Override
        public ModbusPayload createResponse() {
            return new ReadMultipleHoldingRegistersResponse(); 
        }
    },
    READ_INPUT_REGISTERS((byte) 0x04) {
        @Override
        public ModbusPayload createRequest() {
            throw new UnsupportedOperationException("Not supported yet."); 
        }

        @Override
        public ModbusPayload createResponse() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    },
    WRITE_SINGLE_COIL((byte) 0x05) {
        @Override
        public ModbusPayload createRequest() {
            throw new UnsupportedOperationException("Not supported yet."); 
        }

        @Override
        public ModbusPayload createResponse() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    },
    WRITE_SINGLE_HOLDING_REGISTER((byte) 0x06) {
        @Override
        public ModbusPayload createRequest() {
            return new WriteSingleHoldingRegisterRequest(); 
        }

        @Override
        public ModbusPayload createResponse() {
            return new WriteSingleHoldingRegisterResponse();
        }
    },
    WRITE_MULTIPLE_HOLDING_COILS((byte) 0x0f) {
        @Override
        public ModbusPayload createRequest() {
            return new WriteMultipleHoldingRegistersRequest(); 
        }

        @Override
        public ModbusPayload createResponse() {
            return new WriteMultipleHoldingRegistersResponse();
        }
    },
    WRITE_MULTIPLE_HOLDING_REGISTERS((byte) 0x10) {
        @Override
        public ModbusPayload createRequest() {
            return new WriteSingleHoldingRegisterRequest();
        }

        @Override
        public ModbusPayload createResponse() {
            return new WriteSingleHoldingRegisterResponse();
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
    public abstract ModbusPayload createRequest();
    public abstract ModbusPayload createResponse();
    private byte code;
}
