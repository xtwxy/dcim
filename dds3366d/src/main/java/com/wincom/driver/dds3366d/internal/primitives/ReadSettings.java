package com.wincom.driver.dds3366d.internal.primitives;

import com.wincom.dcim.agentd.domain.IntegerSignal;
import com.wincom.dcim.agentd.domain.Signal;
import com.wincom.dcim.agentd.domain.TimstampSignal;
import com.wincom.dcim.agentd.primitives.GetSignalValues;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.StateMachineBuilder;
import com.wincom.driver.dds3366d.internal.ReadSettingsResponseState;
import com.wincom.driver.dds3366d.internal.ReadSettingsRequestState;
import com.wincom.dcim.agentd.primitives.AbstractWireable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author master
 */
public class ReadSettings {

    private static final Set<String> KEYS;

    static {
        KEYS = new HashSet();
        KEYS.add("clock");
        KEYS.add("slaveAddress");
        KEYS.add("pt");
        KEYS.add("ct");
    }

    public static State initial(Message request, HandlerContext replyTo) {
        if (request instanceof GetSignalValues.Request) {
            GetSignalValues.Request r = (GetSignalValues.Request) request;
            HashSet<String> theKeys = new HashSet<>(r.getKeys());
            theKeys.retainAll(KEYS);
            if (theKeys.isEmpty()) {
                return stopState();
            } else {
                return sendRequestState(replyTo);
            }
        }
        return stopState();
    }

    private static State sendRequestState(HandlerContext replyTo) {
        StateMachineBuilder builder = new StateMachineBuilder();
        return builder
                .add("send", new ReadSettingsRequestState())
                .add("receive", new ReadSettingsResponseState(replyTo))
                .add("stop", stopState())
                .transision("send", "receive", "stop", null)
                .transision("receive", "stop", "stop", null)
                .transision("stop", "stop", "stop", null)
                .buildWithInitialAndStop("send", "stop");
    }

    private static State stopState() {
        return new State.Adapter() {
            @Override
            public boolean stopped() {
                return true;
            }
        };
    }

    public static class Response extends AbstractWireable implements Message {

        private short sec;
        private short min;
        private short hour;
        private short week;
        private short date;
        private short month;
        private short year;
        private short slaveAddress;
        private short pt;
        private short ct;

        @Override
        public int getWireLength() {
            return 20;
        }

        @Override
        public void toWire(ByteBuffer buffer) {
            buffer.order(ByteOrder.BIG_ENDIAN);
            buffer.putShort(sec);
            buffer.putShort(min);
            buffer.putShort(hour);
            buffer.putShort(week);
            buffer.putShort(date);
            buffer.putShort(month);
            buffer.putShort(year);
            buffer.putShort(slaveAddress);
            buffer.putShort(pt);
            buffer.putShort(ct);
        }

        @Override
        public void fromWire(ByteBuffer buffer) {
            buffer.order(ByteOrder.BIG_ENDIAN);
            sec = buffer.getShort();
            min = buffer.getShort();
            hour = buffer.getShort();
            week = buffer.getShort();
            date = buffer.getShort();
            month = buffer.getShort();
            year = buffer.getShort();
            slaveAddress = buffer.getShort();
            pt = buffer.getShort();
            ct = buffer.getShort();
        }

        @Override
        public void apply(HandlerContext ctx, Handler handler) {
            final GetSignalValues.Response response = new GetSignalValues.Response();
            final Map<String, Signal> values = response.getValues();

            values.put("clock", new TimstampSignal(getDate()));
            values.put("slaveAddress", new IntegerSignal(Integer.valueOf(getSlaveAddress())));
            values.put("pt", new IntegerSignal(Integer.valueOf(getPt())));
            values.put("ct", new IntegerSignal(Integer.valueOf(getCt())));

            handler.handle(ctx, response);
        }

        public Date getDate() {
            Calendar c = Calendar.getInstance();
            c.clear();
            c.set(Calendar.SECOND, 0xffff & sec);
            c.set(Calendar.MINUTE, 0xffff & min);
            c.set(Calendar.HOUR_OF_DAY, 0xffff & hour);
            c.set(Calendar.DAY_OF_WEEK, 0xffff & week);
            c.set(Calendar.DAY_OF_MONTH, 0xffff & date);
            c.set(Calendar.MONTH, 0xffff & month);
            c.set(Calendar.YEAR, 0xffff & year);

            return c.getTime();
        }

        public void setDate(Date d) {
            Calendar c = Calendar.getInstance();
            c.clear();
            c.setTime(d);

            sec = (short) (0xffff & c.get(Calendar.SECOND));
            min = (short) (0xffff & c.get(Calendar.MINUTE));
            hour = (short) (0xffff & c.get(Calendar.HOUR_OF_DAY));
            week = (short) (0xffff & c.get(Calendar.DAY_OF_WEEK));
            date = (short) (0xffff & c.get(Calendar.DAY_OF_MONTH));
            month = (short) (0xffff & c.get(Calendar.MONTH));
            year = (short) (0xffff & c.get(Calendar.YEAR));
        }

        public short getSlaveAddress() {
            return slaveAddress;
        }

        public void setSlaveAddress(short slaveAddress) {
            this.slaveAddress = slaveAddress;
        }

        public short getPt() {
            return pt;
        }

        public void setPt(short pt) {
            this.pt = pt;
        }

        public short getCt() {
            return ct;
        }

        public void setCt(short ct) {
            this.ct = ct;
        }
    }

}
