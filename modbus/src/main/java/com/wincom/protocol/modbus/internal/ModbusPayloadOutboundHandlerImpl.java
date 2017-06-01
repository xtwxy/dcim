package com.wincom.protocol.modbus.internal;

import com.wincom.dcim.agentd.ChannelOutboundHandler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Accept;
import com.wincom.dcim.agentd.primitives.CloseConnection;
import com.wincom.dcim.agentd.primitives.Connect;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.protocol.modbus.ModbusPayloadOutboundHandler;
import com.wincom.protocol.modbus.ReadMultipleHoldingRegistersRequest;
import com.wincom.protocol.modbus.WriteMultipleHoldingRegistersRequest;
import com.wincom.protocol.modbus.WriteSingleHoldingRegisterRequest;

/**
 *
 * @author master
 */
public class ModbusPayloadOutboundHandlerImpl 
        extends ChannelOutboundHandler.Adapter
        implements ModbusPayloadOutboundHandler {

    @Override
    public void handleReadMultipleHoldingRegistersRequest(HandlerContext ctx, ReadMultipleHoldingRegistersRequest m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleWriteMultipleHoldingRegistersRequest(HandlerContext ctx, WriteMultipleHoldingRegistersRequest m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleWriteSingleHoldingRegisterRequest(HandlerContext ctx, WriteSingleHoldingRegisterRequest m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleAccept(HandlerContext ctx, Accept m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleConnect(HandlerContext ctx, Connect m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleClose(HandlerContext ctx, CloseConnection m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleSendPayload(HandlerContext ctx, Message m) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setOutbound(HandlerContext ctx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
