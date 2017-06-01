package com.wincom.dcim.agentd.statemachine;

import com.wincom.dcim.agentd.HandlerContext;
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
            if (current != null) {
                current = current.enter(ctx);
            } else {
                log.warn("current == null");
            }
        }
    }

    @Override
    public synchronized State on(HandlerContext ctx, Message m) {
        prev = current;
        current = current.on(ctx, m);
        if (null == current) {
            log.warn("current == null");
        }

        transition(ctx);

        return current;
    }

    @Override
    public boolean stopped() {
        return current.stopped();
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
    public State failure(State s) {
        return current.failure(s);
    }

    @Override
    public State failure() {
        return current.failure();
    }

    @Override
    public State error(State s) {
        return current.error(s);
    }

    @Override
    public State error() {
        return current.error();
    }

    public State initial() {
        return this.initial;
    }

    public State stop() {
        return this.stop;
    }
}
