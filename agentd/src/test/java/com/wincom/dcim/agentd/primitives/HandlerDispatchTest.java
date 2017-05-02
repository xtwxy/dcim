package com.wincom.dcim.agentd.primitives;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author master
 */
public class HandlerDispatchTest {
    private int method = -1;
    private final Handler handler = new Handler() {
        @Override
        public void handleGetSignalValuesRequest(GetSignalValues.Request r) {
            method = 0;
        }

        @Override
        public void handleGetSignalValuesRequest(GetSignalValues.Response r) {
            method = 1;
        }

        @Override
        public void handleSetSignalValuesRequest(SetSignalValues.Request r) {
            method = 2;
        }

        @Override
        public void handleSetSignalValuesResponse(SetSignalValues.Response r) {
            method = 3;
        }

        @Override
        public void handlePushEvents(PushEvents pe) {
            method = 4;
        }

        @Override
        public void apply(Message m) {
            method = 5;
        }
    };

    @Test
    public void testGetSignalValuesRequest() {
        Message msg = new GetSignalValues.Request();
        msg.apply(handler);
        assertEquals(0, method);
    }

    @Test
    public void testGetSignalValuesResponse() {
        Message msg = new GetSignalValues.Response();
        msg.apply(handler);
        assertEquals(1, method);
    }

    @Test
    public void testSetSignalValuesRequest() {
        Message msg = new SetSignalValues.Request();
        msg.apply(handler);
        assertEquals(2, method);
    }

    @Test
    public void testSetSignalValuesResponse() {
        Message msg = new SetSignalValues.Response();
        msg.apply(handler);
        assertEquals(3, method);
    }
    
    @Test
    public void testPushEvents() {
        Message msg = new PushEvents();
        msg.apply(handler);
        assertEquals(4, method);
    }
}
