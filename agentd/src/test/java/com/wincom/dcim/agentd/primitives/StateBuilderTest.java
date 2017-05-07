package com.wincom.dcim.agentd.primitives;

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
            out.printf("step: %d, \n", step);
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

        State initial = builder.build();

        out.println("Start: ");
        runStateMachine(initial);
        out.println("Stop.");
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

        State initial = builder.build();

        out.println("Start: ");
        runStateMachine(initial);
        out.println("Stop.");
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

        State initial = builder.build();

        out.println("Start: ");
        runStateMachine(initial);
        out.println("Stop.");
        assertEquals(2, step);
    }

    private void runStateMachine(State initial) {
        State sm = new StateMachine(initial);
        sm.enter();
        while(!sm.stopped()) {
            sm.on(message);
        }
    }
    private void runStateMachine0(State sm) {
        State current = sm;
        State prev = null;

        while (!current.stopped()) {
            if (prev != current) {
                current.enter();
            }

            prev = current;
            current = current.on(message);
            
            if (prev != current && prev != null) {
                prev.exit();
            }
        }
    }
}
