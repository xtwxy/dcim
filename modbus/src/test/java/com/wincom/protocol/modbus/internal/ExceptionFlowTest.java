package com.wincom.protocol.modbus.internal;

import org.junit.Test;
import static java.lang.System.out;

/**
 *
 * @author master
 */
public class ExceptionFlowTest {

    @Test
    public void test() {
        for(int i = 0; i < 10; ++i) {
            out.print("" + i +":\t");
            exceptionByRandom();
        }
    }

    private void exceptionByRandom() {
        int i = (int) (Math.round(Math.random() * 100) % 2);
        switch (i) {
            case 0:
            case 1:
                try {
                    if (i == 0) {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                } finally {
                    out.println("finally.");
                }
                out.println("no exception.");
                break;

        }
    }
}
