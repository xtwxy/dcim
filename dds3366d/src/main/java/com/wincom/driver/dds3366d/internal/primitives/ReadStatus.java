package com.wincom.driver.dds3366d.internal.primitives;

import com.wincom.dcim.agentd.primitives.GetSignalValues;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.dcim.agentd.statemachine.StateMachineBuilder;
import com.wincom.driver.dds3366d.internal.ReadStatusRequestState;
import com.wincom.driver.dds3366d.internal.ReadStatusResponseState;
import com.wincom.protocol.modbus.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Set;

/**
 * @author master
 */
public class ReadStatus {

    private final Set<String> keys;

    public ReadStatus() {
        keys = new HashSet();
        keys.add("activePowerCombo");
        keys.add("positiveActivePower");
        keys.add("reverseActivePower");
        keys.add("voltage");
        keys.add("current");
        keys.add("power");
        keys.add("powerFactor");
        keys.add("frequency");
    }
    public State initial(Message request, HandlerContext replyTo) {
        if (request instanceof GetSignalValues.Request) {
            GetSignalValues.Request r = (GetSignalValues.Request) request;
            HashSet<String> theKeys = new HashSet<>(r.getKeys());
            theKeys.retainAll(keys);
            if (theKeys.isEmpty()) {
                return stopState();
            } else {
                return sendRequestState(replyTo);
            }
        }
        return stopState();
    }

    private State sendRequestState(HandlerContext replyTo) {
        StateMachineBuilder builder = new StateMachineBuilder();
        return builder
                .add("send", new ReadStatusRequestState())
                .add("receive", new ReadStatusResponseState(replyTo))
                .add("stop", stopState())
                .transision("send", "receive", "stop")
                .transision("receive", "stop", "stop")
                .transision("stop", "stop", "stop")
                .buildWithInitialAndStop("send", "stop");
    }

    private State stopState() {
        return new State.Adapter();
    }

    public static class Request implements Message {

        @Override
        public void apply(HandlerContext ctx, Handler handler) {
            handler.handle(ctx, this);
        }

        @Override
        public boolean isOob() {
            return false;
        }
    }

    public static class Response extends AbstractWireable implements Message {

        private int activePowerCombo;
        private int positiveActivePower;
        private int reverseActivePower;
        private short voltage;
        private int current;
        private int power;
        private short powerFactor;
        private short frequency;

        @Override
        public int getWireLength() {
            return 4 // private int activePowerCombo;
                    + 4 // private int positiveActivePower;
                    + 4 // private int reverseActivePower;
                    + 2 // private short voltage;
                    + 4 // private int current;
                    + 4 // private int power;
                    + 2 // private short powerFactor;
                    + 2 // private short frequency;
                    ;
        }

        @Override
        public void toWire(ByteBuffer buffer) {
            buffer.order(ByteOrder.BIG_ENDIAN);
            buffer.putInt(activePowerCombo);
            buffer.putInt(positiveActivePower);
            buffer.putInt(reverseActivePower);
            buffer.putShort(voltage);
            buffer.putInt(current);
            buffer.putInt(power);
            buffer.putShort(powerFactor);
            buffer.putShort(frequency);
        }

        @Override
        public void fromWire(ByteBuffer buffer) {
            buffer.order(ByteOrder.BIG_ENDIAN);
            activePowerCombo = buffer.getInt();
            positiveActivePower = buffer.getInt();
            reverseActivePower = buffer.getInt();
            voltage = buffer.getShort();
            current = buffer.getInt();
            power = buffer.getInt();
            powerFactor = buffer.getShort();
            frequency = buffer.getShort();
        }

        @Override
        public void apply(HandlerContext ctx, Handler handler) {
            handler.handle(ctx, this);
        }

        public double getActivePowerCombo() {
            return activePowerCombo * 0.01;
        }

        public void setActivePowerCombo(double activePowerCombo) {
            this.activePowerCombo = (int) Math.round(activePowerCombo / 0.01);
        }

        public double getPositiveActivePower() {
            return positiveActivePower * 0.01;
        }

        public void setPositiveActivePower(double positiveActivePower) {
            this.positiveActivePower = (int) Math.round(positiveActivePower / 0.01);
            ;
        }

        public double getReverseActivePower() {
            return reverseActivePower * 0.01;
        }

        public void setReverseActivePower(double reverseActivePower) {
            this.reverseActivePower = (int) Math.round(reverseActivePower / 0.01);
        }

        public double getVoltage() {
            return voltage * 0.01;
        }

        public void setVoltage(double voltage) {
            this.voltage = (short) Math.round(voltage / 0.01);
        }

        public double getCurrent() {
            return current * 0.001;
        }

        public void setCurrent(double current) {
            this.current = (int) Math.round(current / 0.001);
        }

        public double getPower() {
            return power * 0.0001;
        }

        public void setPower(double power) {
            this.power = (int) Math.round(power / 0.0001);
        }

        public double getPowerFactor() {
            return powerFactor * 0.001;
        }

        public void setPowerFactor(double powerFactor) {
            this.powerFactor = (short) Math.round(powerFactor / 0.001);
        }

        public double getFrequency() {
            return frequency * 0.01;
        }

        public void setFrequency(double frequency) {
            this.frequency = (short) Math.round(frequency / 0.01);
        }

        @Override
        public boolean isOob() {
            return false;
        }

    }

}
