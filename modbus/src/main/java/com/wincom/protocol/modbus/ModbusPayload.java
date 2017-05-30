package com.wincom.protocol.modbus;

import com.wincom.dcim.agentd.primitives.Wireable;
import com.wincom.dcim.agentd.primitives.Message;

/**
 *
 * @author master
 */
public interface ModbusPayload extends Wireable, Message {
    public ModbusFunction getFunctionCode();
}
