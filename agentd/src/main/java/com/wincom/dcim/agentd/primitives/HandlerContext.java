package com.wincom.dcim.agentd.primitives;

import com.wincom.dcim.agentd.statemachine.StateMachine;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author master
 */
public interface HandlerContext {

    /**
     * Send message via the underlying service.
     *
     * @param m
     */
    public void send(Message m);

    /**
     * Fire message from the underlying service to the state machine.
     *
     * @param m
     */
    public void fire(Message m);

    /**
     * Set context variables.
     *
     * @param key
     * @param value
     */
    public void set(Object key, Object value);

    /**
     * Get context variables.
     *
     * @param key
     * @return
     */
    public Object get(Object key);

    /**
     * Get state machine.
     *
     * @return
     */
    public StateMachine getStateMachine();
    /**
     * Set state machine.
     *
     * @param sm
     */
    public void setStateMachine(StateMachine sm);

    public static abstract class Adapter implements HandlerContext {

        private StateMachine machine;
        private Map<Object, Object> variables;

        public Adapter(StateMachine machine) {
            this.machine = machine;
            this.variables = new HashMap<>();
        }

        @Override
        public void set(Object key, Object value) {
            variables.put(key, value);
        }

        @Override
        public Object get(Object key) {
            return variables.get(key);
        }

        @Override
        public StateMachine getStateMachine() {
            return machine;
        }

        @Override
        public void setStateMachine(StateMachine sm) {
            machine = sm;
        }

    }
}
