package com.wincom.dcim.agentd.internal;

import com.wincom.dcim.agentd.primitives.Handler;
import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import com.wincom.dcim.agentd.statemachine.State;
import com.wincom.dcim.agentd.statemachine.StateBuilder;
import com.wincom.dcim.agentd.statemachine.StateMachine;
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
        public void apply(HandlerContext ctx, Handler handler) {
            handler.handle(null, this);
        }

        @Override
        public boolean isOob() {
            return false;
        }

    };
    private final Handler handler = new Handler() {
        @Override
        public void handle(HandlerContext ctx, Message m) {
            step++;
            out.printf("step: %d, \n", step);
        }
    };
    
    @Test
    public void testBuildStateMachine0() {

        StateBuilder builder = StateBuilder
                .initial().state(new State.Adapter() {
                    @Override
                    public State on(HandlerContext ctx, Message m) {
                        m.apply(ctx, handler);
                        return success();
                    }
                })
                .success().state(new State.Adapter() {
                    @Override
                    public State on(HandlerContext ctx, Message m) {
                        m.apply(ctx, handler);
                        return success();
                    }
                })
                .success().state(new State.Adapter() {
                    @Override
                    public State on(HandlerContext ctx, Message m) {
                        m.apply(ctx, handler);
                        return success();
                    }
                });

        StateMachine sm = new StateMachine(builder);

        out.println("Start: ");
        runStateMachine(sm);
        out.println("Stop.");
        assertEquals(3, step);
    }

    @Test
    public void testBuildStateMachine1() {

        StateBuilder builder = StateBuilder
                .initial().state(new State.Adapter() {
                    @Override
                    public State on(HandlerContext ctx, Message m) {
                        m.apply(ctx, handler);
                        return success();
                    }
                })
                .success().state(new State.Adapter() {
                    @Override
                    public State on(HandlerContext ctx, Message m) {
                        m.apply(ctx, handler);
                        return fail();
                    }
                })
                .fail().state(new State.Adapter() {
                    @Override
                    public State on(HandlerContext ctx, Message m) {
                        m.apply(ctx, handler);
                        return success();
                    }
                })
                .success().state(new State.Adapter() {
                    @Override
                    public State on(HandlerContext ctx, Message m) {
                        m.apply(ctx, handler);
                        return success();
                    }
                });
        
        StateMachine sm = new StateMachine(builder);

        out.println("Start: ");
        runStateMachine(sm);
        out.println("Stop.");
        assertEquals(4, step);
    }

    @Test
    public void testBuildStateMachine2() {

        StateBuilder builder = StateBuilder
                .initial().state(new State.Adapter() {
                    @Override
                    public State on(HandlerContext ctx, Message m) {
                        m.apply(ctx, handler);
                        return success();
                    }
                })
                .success().state(new State.Adapter() {
                    @Override
                    public State on(HandlerContext ctx, Message m) {
                        m.apply(ctx, handler);
                        return fail();
                    }
                })
                .success().state(new State.Adapter() {
                    @Override
                    public State on(HandlerContext ctx, Message m) {
                        m.apply(ctx, handler);
                        return success();
                    }
                });

        StateMachine sm = new StateMachine(builder);

        out.println("Start: ");
        runStateMachine(sm);
        out.println("Stop.");
        assertEquals(2, step);
    }

    private void runStateMachine(StateMachine sm) {
        HandlerContext ctx = new StreamHandlerContextImpl(sm, null, null);
        sm.enter(ctx);
        while(!sm.stopped()) {
            sm.on(ctx, message);
        }
    }
    private void runStateMachine0(StateMachine sm) {
        HandlerContext ctx = new StreamHandlerContextImpl(sm, null, null);

        State current = sm.initial();
        State prev = null;

        while (!current.stopped()) {
            if (prev != current) {
                current.enter(ctx);
            }

            prev = current;
            current = current.on(ctx, message);
            
            if (prev != current && prev != null) {
                prev.exit(ctx);
            }
        }
    }

    @Override
    public String toString() {
        return "StateBuilderTest@" + this.hashCode();
    }
}
