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
        public void handle(Message m) {
            method = 0;
        }
    };

    @Test
    public void testGetSignalValuesRequest() {
        Message msg = new GetSignalValues.Request();
        msg.apply(handler);
        assertEquals(0, method);
    }
}
