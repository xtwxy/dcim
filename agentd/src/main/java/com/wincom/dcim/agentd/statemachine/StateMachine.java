package com.wincom.dcim.agentd.statemachine;

import com.wincom.dcim.agentd.primitives.HandlerContext;
import com.wincom.dcim.agentd.primitives.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * State Machine.
 *
 * Created by master on 5/4/17.
 */
public class StateMachine implements State {

    Logger log = LoggerFactory.getLogger(this.getClass());

    State current;
    State prev;
    State initial;
    State stop;

    public StateMachine() {
    }

    public StateMachine(
            State initial
    ) {
        this();
        this.initial = initial;
        this.stop = new State.Adapter();

        this.current = initial;
        this.prev = null;
    }

    public StateMachine(
            State initial,
            State stop
    ) {
        this();
        this.initial = initial;
        this.stop = stop;

        this.current = initial;
        this.prev = null;
    }

    public StateMachine(StateBuilder builder) {
        buildWith(builder);
    }

    public final StateMachine buildWith(StateBuilder builder) {
        this.initial = builder.build();
        this.stop = builder.stopState();

        this.current = this.initial;
        this.prev = null;

        return this;
    }

    public final StateMachine buildWith(StateMachine m) {
        this.initial = m.initial;
        this.stop = m.stop;

        this.current = m.current;
        this.prev = m.prev;

        return this;
    }

    @Override
    public State enter() {
        if (prev != current) {
            try {
                return current.enter();
            } catch (Throwable t) {
                return current.fail();
            }
        }
        return current.fail();
    }

    @Override
    public State exit() {
        if (prev != current && prev != null) {
            try {
                return prev.exit();
            } catch (Throwable t) {
                return current.fail();
            }
        }
        return current.fail();
    }

    private void transition() {
        enter();
        exit();
    }

    @Override
    public State on(HandlerContext ctx, Message m) {
        prev = current;
        current = current.on(ctx, m);

        transition();

        return current;
    }

    @Override
    public boolean stopped() {
        return current.stopped();
    }

    @Override
    public State next() {
        return current.next();
    }

    @Override
    public State success(State s) {
        return current.success(s);
    }

    @Override
    public State success() {
        return current.success();
    }

    @Override
    public State fail(State s) {
        return current.fail(s);
    }

    @Override
    public State fail() {
        return current.fail();
    }

    public State initial() {
        return this.initial;
    }

    public State stop() {
        return this.stop;
    }
}
