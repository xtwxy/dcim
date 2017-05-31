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
        current = new State.Adapter();
        prev = current;
        initial = current;
        stop = current;
    }

    public StateMachine(
            State initial
    ) {
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
    public State enter(HandlerContext ctx) {
        prev = current;
        current = current.enter(ctx);
        return current;
    }

    @Override
    public void exit(HandlerContext ctx) {
        prev.exit(ctx);
    }

    private void transition(HandlerContext ctx) {
        if (prev != current) {
            if (prev != null) {
                prev.exit(ctx);
            }

            current = current.enter(ctx);
        }
    }

    @Override
    public synchronized State on(HandlerContext ctx, Message m) {
        prev = current;
        current = current.on(ctx, m);

        transition(ctx);

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
