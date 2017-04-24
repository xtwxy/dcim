package com.wincom.protocol.modbus;

import com.google.common.primitives.UnsignedBytes;

/**
 *
 * @author master
 */
public enum ModbusFunction {
    READ_COILS((byte) 0x01),
    READ_DISCRETE_INPUTS((byte) 0x02),
    READ_MULTIPLE_HOLDING_REGISTERS((byte) 0x03),
    READ_INPUT_REGISTERS((byte) 0x04),
    WRITE_SINGLE_COIL((byte) 0x05),
    WRITE_SINGLE_HOLDING_REGISTERS((byte) 0x06),
    WRITE_MULTIPLE_HOLDING_COILS((byte) 0x15),
    WRITE_MULTIPLE_HOLDING_REGISTERS((byte) 0x16);

    ModbusFunction(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }
    public static ModbusFunction from(byte b) {
        for(ModbusFunction c : ModbusFunction.class.getEnumConstants()) {
            if(c.getCode() == b) {
                return c;
            }
        }
        throw new IllegalArgumentException("Unknown modbus function code 0x" + UnsignedBytes.toString(b, 16));
    }
    
    private byte code;
}
