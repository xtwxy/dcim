package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.messages.BytesReceived;
import com.wincom.dcim.agentd.messages.ApplicationFailure;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.ChannelInactive;
import com.wincom.dcim.agentd.messages.ChannelTimeout;
import com.wincom.dcim.agentd.messages.Message;
import com.wincom.dcim.agentd.messages.SendBytes;
import com.wincom.dcim.agentd.messages.SystemError;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.driver.dds3366d.internal.primitives.ReadSettings;
import com.wincom.driver.dds3366d.internal.primitives.ReadStatus;
import com.wincom.protocol.modbus.ModbusFrame;
import com.wincom.protocol.modbus.ModbusFunction;
import com.wincom.protocol.modbus.ReadMultipleHoldingRegistersRequest;
import com.wincom.protocol.modbus.ReadMultipleHoldingRegistersResponse;
import java.nio.ByteBuffer;
import java.util.Date;

/**
 *
 * @author master
 */
public class SlaveReceiveRequestState extends State.Adapter {

    private static final String READ_BUFFER_KEY = "READ_BUFFER";

    private ByteBuffer readBuffer;

    @Override
    public State enter(HandlerContext ctx) {
        readBuffer = (ByteBuffer) ctx.getOrSetIfNotExist(READ_BUFFER_KEY, ByteBuffer.allocate(2048));
        return this;
    }

    @Override
    public State on(HandlerContext ctx, Message m) {

        if (m instanceof BytesReceived) {
            BytesReceived b = (BytesReceived) m;
            readBuffer.put(b.getByteBuffer());
            readBuffer.flip();
            decode(ctx, readBuffer);
        } else if (m instanceof ChannelInactive
                || m instanceof ApplicationFailure
                || m instanceof SystemError) {
            readBuffer.clear();
            readBuffer.compact();
            readBuffer.flip();
            return failure();
        } else if (m instanceof ChannelTimeout) {
            readBuffer.clear();
            readBuffer.compact();
            readBuffer.flip();
        }
        return success();
    }

    private void decode(HandlerContext ctx, ByteBuffer readBuffer) {
        byte[] src = readBuffer.array();
        if (readBuffer.remaining() < 2) {
            return;
        }
        switch (ModbusFunction.from(src[1])) {
            case READ_COILS:
                break;
            case READ_DISCRETE_INPUTS:
                break;
            case READ_MULTIPLE_HOLDING_REGISTERS:
                handleReadMultipleHoldingRegisters(ctx, readBuffer);
                break;
            case READ_INPUT_REGISTERS:
                break;
            case WRITE_SINGLE_COIL:
                break;
            case WRITE_MULTIPLE_HOLDING_COILS:
                break;
            case WRITE_SINGLE_HOLDING_REGISTER:
            case WRITE_MULTIPLE_HOLDING_REGISTERS:
                if (readBuffer.remaining() < 8) {
                    break;
                }

                src = readBuffer.array();
                decode(ctx, ByteBuffer.wrap(src, 0, 8));
                break;
            default:
                // TODO: handle default case.
                break;
        }
    }

    private void handleReadMultipleHoldingRegisters(HandlerContext ctx, ByteBuffer readBuffer) {
        while (true) {
            if (readBuffer.remaining() >= 8) {
                ModbusFrame request = new ModbusFrame(ctx, true);
                try {
                    request.fromWire(readBuffer.duplicate());
                    ReadMultipleHoldingRegistersRequest payload = (ReadMultipleHoldingRegistersRequest) request.getPayload();
                    if (0x0000 == payload.getStartAddress()) {
                        sendReadStatusResponse(ctx, request);
                    } else if (0x01f4 == payload.getStartAddress()) {
                        sendReadSettingsResponse(ctx, request);
                    } else {
                        // unknown.
                    }
                    readBuffer.position(request.getWireLength());
                    readBuffer.compact();
                    readBuffer.flip();
                } catch (Exception ex) {
                    readBuffer.position(1);
                    readBuffer.compact();
                }
            } else {
                readBuffer.compact();
                return;
            }
        }
    }

    private void sendReadSettingsResponse(HandlerContext ctx, ModbusFrame request) {
        ModbusFrame response = new ModbusFrame(ctx, false);
        response.setSlaveAddress(request.getSlaveAddress());
        ReadSettings.Response status = new ReadSettings.Response(ctx);
        status.setDate(new Date());
        status.setCt((short)100);
        status.setPt((short)200);
        status.setSlaveAddress(request.getSlaveAddress());
        ByteBuffer writeBuffer = ByteBuffer.allocate(status.getWireLength());
        status.toWire(writeBuffer);
        writeBuffer.flip();

        ReadMultipleHoldingRegistersResponse payload = new ReadMultipleHoldingRegistersResponse(ctx);
        payload.setBytes(writeBuffer.array());
        response.setPayload(payload);
        writeBuffer = ByteBuffer.allocate(response.getWireLength());
        response.toWire(writeBuffer);
        writeBuffer.flip();
        ctx.send(new SendBytes(ctx, writeBuffer));
    }

    private void sendReadStatusResponse(HandlerContext ctx, ModbusFrame request) {
        ModbusFrame response = new ModbusFrame(ctx, false);
        response.setSlaveAddress(request.getSlaveAddress());
        ReadStatus.Response status = new ReadStatus.Response(ctx);
        status.setActivePowerCombo(123);
        status.setCurrent(456);
        status.setFrequency(789);
        status.setPositiveActivePower(1234);
        status.setPower(5678);
        status.setPowerFactor(0.8);
        status.setReverseActivePower(9012);
        status.setVoltage(222);
        ByteBuffer writeBuffer = ByteBuffer.allocate(status.getWireLength());
        status.toWire(writeBuffer);
        writeBuffer.flip();

        ReadMultipleHoldingRegistersResponse payload = new ReadMultipleHoldingRegistersResponse(ctx);
        payload.setBytes(writeBuffer.array());
        response.setPayload(payload);
        writeBuffer = ByteBuffer.allocate(response.getWireLength());
        response.toWire(writeBuffer);
        writeBuffer.flip();
        ctx.send(new SendBytes(ctx, writeBuffer));
    }
}
