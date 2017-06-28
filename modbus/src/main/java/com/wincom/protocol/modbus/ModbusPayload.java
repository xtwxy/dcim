package com.wincom.protocol.modbus;

import com.wincom.dcim.agentd.messages.Wireable;
import com.wincom.dcim.agentd.messages.Message;

/**
 *
 * @author master
 */
public interface ModbusPayload extends Wireable, Message {
    ModbusFunction getFunctionCode();
}
