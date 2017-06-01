package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.HandlerContext;

/**
 *
 * @author master
 */
public class ModbusSlaveContextImpl extends HandlerContext.Adapter {

    private final byte slaveAddress;
    private final ModbusDecodeContextImpl delegate;

    public ModbusSlaveContextImpl(byte slaveAddress, ModbusDecodeContextImpl delegate) {
        this.slaveAddress = slaveAddress;
        this.delegate = delegate;
    }
}
