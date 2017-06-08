package com.wincom.driver.dds3366d.internal;

import com.wincom.dcim.agentd.primitives.BytesReceived;
import com.wincom.dcim.agentd.primitives.ApplicationFailure;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.ChannelInactive;
import com.wincom.dcim.agentd.primitives.ChannelTimeout;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.primitives.SendBytes;
import com.wincom.dcim.agentd.primitives.SystemError;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.driver.dds3366d.internal.primitives.ReadStatus;
import com.wincom.protocol.modbus.ModbusFrame;
import com.wincom.protocol.modbus.ModbusFunction;
import static com.wincom.protocol.modbus.ModbusFunction.READ_COILS;
import static com.wincom.protocol.modbus.ModbusFunction.READ_DISCRETE_INPUTS;
import static com.wincom.protocol.modbus.ModbusFunction.READ_INPUT_REGISTERS;
import static com.wincom.protocol.modbus.ModbusFunction.READ_MULTIPLE_HOLDING_REGISTERS;
import static com.wincom.protocol.modbus.ModbusFunction.WRITE_MULTIPLE_HOLDING_COILS;
import static com.wincom.protocol.modbus.ModbusFunction.WRITE_MULTIPLE_HOLDING_REGISTERS;
import static com.wincom.protocol.modbus.ModbusFunction.WRITE_SINGLE_COIL;
import static com.wincom.protocol.modbus.ModbusFunction.WRITE_SINGLE_HOLDING_REGISTER;
import com.wincom.protocol.modbus.ReadMultipleHoldingRegistersResponse;
import java.nio.ByteBuffer;

/**
 *
 * @author master
 */
public class SlaveReceiveRequestState extends State.Adapter {

    public static final String READ_BUFFER_KEY = "READ_BUFFER";

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

    public void handleReadMultipleHoldingRegisters(HandlerContext ctx, ByteBuffer readBuffer) {
        while (true) {
            if (readBuffer.remaining() >= 8) {
                ModbusFrame request = new ModbusFrame(ctx, true);
                try {
                    request.fromWire(readBuffer.duplicate());

                    readBuffer.position(request.getWireLength());
                    readBuffer.compact();
                    readBuffer.flip();

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
}
