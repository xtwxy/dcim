package com.wincom.driver.dds3366d.internal.primitives;

import com.wincom.dcim.agentd.primitives.GetSignalValues;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.StateBuilder;
import com.wincom.protocol.modbus.AbstractWireable;

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

    private final Set<String> keys;
    private final Map<Class, Handler> handlers;

    public ReadSettings(Map<Class, Handler> handlers) {
        keys = new HashSet();
        keys.add("clock");
        keys.add("slaveAddress");
        keys.add("pt");
        keys.add("ct");
        
        this.handlers = handlers;
    }

    public State initial(Message m) {
        if (m instanceof GetSignalValues.Request) {
            GetSignalValues.Request r = (GetSignalValues.Request) m;
            HashSet<String> theKeys = new HashSet<>(r.getKeys());
            theKeys.retainAll(keys);
            if (theKeys.isEmpty()) {
                return stopState();
            } else {
                return sendRequestState();
            }
        }
        return stopState();
    }

    private State sendRequestState() {
        StateBuilder builder = StateBuilder
                .initial().state(new State.Adapter() {
                    @Override
                    public State enter(HandlerContext ctx) {
                        // install request completion handler.
                        // send request
                        Request r = new Request();
                        r.apply(null, handlers.get(Request.class));
                    }

                    @Override
                    public State on(Message m) {
                        // wait for send request to complete.
                        return success();
                    }

                    @Override
                    public State exit(HandlerContext ctx) {
                        // un-install request completion handler.
                    }
                })
                .success().state(new State.Adapter() {
                    @Override
                    public State enter(HandlerContext ctx) {
                        // install response handler
                    }

                    @Override
                    public State on(Message m) {
                        // wait for response
                        if(m instanceof Response) {
                            Response r = (Response) m;
                            r.apply(null, handlers.get(Response.class));
                        }
                        return success();
                    }

                    @Override
                    public State exit(HandlerContext ctx) {
                        // remove response handler.
                    }
                });
        return builder.build();
    }

    private State stopState() {
        return new State.Adapter();
    }

    public static class Request implements Message {

        @Override
        public void apply(HandlerContext ctx, Handler handler) {
            handler.handle(null, this);
        }
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
            handler.handle(null, this);
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
