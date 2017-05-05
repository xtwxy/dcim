package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.primitives.State;
import com.wincom.dcim.agentd.primitives.StateBuilder;
import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.Message;
import static java.lang.System.out;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author master
 */
public class StateBuilderTest {

    private int step = 0;
    private final Message message = new Message() {
        @Override
        public void apply(Handler handler) {
            handler.handle(this);
        }

    };
    private final Handler handler = new Handler() {
        @Override
        public void handle(Message m) {
            step++;
            out.println("success step: " + step);
        }
    };

    @Test
    public void testBuildStateMachine0() {

        StateBuilder builder = StateBuilder
                .initial().state(new State.Adapter() {
                    @Override
                    public State on(Message m) {
                        m.apply(handler);
                        return success();
                    }
                })
                .success().state(new State.Adapter() {
                    @Override
                    public State on(Message m) {
                        m.apply(handler);
                        return success();
                    }
                })
                .success().state(new State.Adapter() {
                    @Override
                    public State on(Message m) {
                        m.apply(handler);
                        return success();
                    }
                });

        State sm = builder.build();

        State current = sm;
        while (!current.stopped()) {
            current = current.on(message);
        }
        assertEquals(3, step);
    }
    @Test
    public void testBuildStateMachine1() {

        StateBuilder builder = StateBuilder
                .initial().state(new State.Adapter() {
                    @Override
                    public State on(Message m) {
                        m.apply(handler);
                        return success();
                    }
                })
                .success().state(new State.Adapter() {
                    @Override
                    public State on(Message m) {
                        m.apply(handler);
                        return fail();
                    }
                })
                .fail().state(new State.Adapter() {
                    @Override
                    public State on(Message m) {
                        m.apply(handler);
                        return success();
                    }
                })
                .success().state(new State.Adapter() {
                    @Override
                    public State on(Message m) {
                        m.apply(handler);
                        return success();
                    }
                });

        State sm = builder.build();

        State current = sm;
        while (!current.stopped()) {
            current = current.on(message);
        }
        assertEquals(4, step);
    }
    @Test
    public void testBuildStateMachine2() {

        StateBuilder builder = StateBuilder
                .initial().state(new State.Adapter() {
                    @Override
                    public State on(Message m) {
                        m.apply(handler);
                        return success();
                    }
                })
                .success().state(new State.Adapter() {
                    @Override
                    public State on(Message m) {
                        m.apply(handler);
                        return fail();
                    }
                })
                .success().state(new State.Adapter() {
                    @Override
                    public State on(Message m) {
                        m.apply(handler);
                        return success();
                    }
                });

        State sm = builder.build();

        State current = sm;
        while (!current.stopped()) {
            current = current.on(message);
        }
        assertEquals(2, step);
    }
}
