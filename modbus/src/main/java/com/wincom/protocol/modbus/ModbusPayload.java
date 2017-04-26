/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wincom.protocol.modbus;

/**
 *
 * @author master
 */
public interface ModbusPayload extends Wireable {
    public ModbusFunction getFunctionCode();
}
