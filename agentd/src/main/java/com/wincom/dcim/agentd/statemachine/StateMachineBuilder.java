package com.wincom.dcim.agentd.statemachine;

import java.util.HashMap;
import java.util.Map;

/**
 * State Machine.
 *
 * Created by master on 5/4/17.
 */
public class StateMachineBuilder {

    private final Map<String, State> states;

    public StateMachineBuilder() {
        this.states = new HashMap<>();
    }

    public StateMachineBuilder add(String name, State s) {
        if (s instanceof StateMachine) {
            StateMachine sm = (StateMachine) s;
            this.states.put(name, sm.initial());
        } else {
            this.states.put(name, s);
        }

        return this;
    }

    public StateMachineBuilder transision(String name, String success, String failure, String error) {
        State state = this.states.get(name);
        State s = this.states.get(success);
        State f = this.states.get(failure);
        State e = this.states.get(error);
        state.success(s).failure(f).error(e);

        return this;
    }

    public StateMachine buildWithInitialState(String initial) {
        return new StateMachine(this.states.get(initial));
    }

    public StateMachine buildWithInitialAndStop(String initial, String stop) {
        return new StateMachine(this.states.get(initial), this.states.get(stop));
    }

    public State get(String name) {
        return this.states.get(name);
    }
}
