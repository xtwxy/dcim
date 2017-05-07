package com.wincom.dcim.agentd.primitives;

/**
 * State Machine.
 *
 * Created by master on 5/4/17.
 */
public class StateMachine implements State {

    State current;
    State prev;

    public StateMachine(State s) {
        this.current = s;
        this.prev = null;
    }

    @Override
    public void enter() {
        if (prev != current) {
            current.enter();
        }
    }

    @Override
    public void exit() {
        if (prev != current && prev != null) {
            prev.exit();
        }
    }

    private void transition() {
        if (prev != current) {
            current.enter();
        }
        if (prev != current && prev != null) {
            prev.exit();
        }
    }

    @Override
    public State on(Message m) {
        prev = current;
        current = current.on(m);

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

}
