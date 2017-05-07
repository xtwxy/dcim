package com.wincom.dcim.agentd.primitives;

import static java.lang.System.out;

/**
 * State/State Machine.
 *
 * Created by master on 5/4/17.
 */
public interface State {

    /**
     * Life cycle event handler for enter this state.
     */
    public void enter();

    /**
     * Life cycle event handler for exit this state.
     */
    public void exit();

    /**
     * Accept incoming <code>Message</code>.
     *
     * @param m
     * @return
     */
    public State on(Message m);

    /**
     * Test if it is stopped state.
     *
     * @return
     */
    public boolean stopped();

    /**
     * Get the next Determined <code>State</code>.
     *
     * @return
     */
    public State next();

    /**
     * Set the next <code>State</code> for after current state is completed
     * successfully.
     *
     * @param s
     * @return
     */
    public State success(State s);

    /**
     * Get the next <code>State</code> for after current state is completed
     * successfully.
     *
     * @return
     */
    public State success();

    /**
     * Set the next <code>State</code> for after current state is completed with
     * failure.
     *
     * @param s
     * @return
     */
    public State fail(State s);

    /**
     * Get the next <code>State</code> for after current state is completed with
     * failure.
     *
     * @return
     */
    public State fail();

    public static class Adapter implements State {

        protected State next;
        private State success;
        private State fail;

        public Adapter() {
            this.next = this;
        }

        public Adapter(State success, State fail) {
            this.next = this;
            this.success = success;
            this.fail = fail;
        }

        @Override
        public State on(Message m) {
            return this;
        }

        @Override
        public boolean stopped() {
            return success == null;
        }

        @Override
        public State next() {
            return next;
        }

        @Override
        public State success(State s) {
            this.success = s;
            return this;
        }

        @Override
        public State fail(State s) {
            this.fail = s;
            return this;
        }

        @Override
        public State fail() {
            if (this.fail != null) {
                return this.fail;
            } else if (this.success != null) {
                return this.success.fail();
            } else {
                return null;
            }
        }

        @Override
        public State success() {
            return this.success;
        }

        @Override
        public void enter() {
            out.println("enter steps:");
        }

        @Override
        public void exit() {
            out.println("exit.");
        }
    }
}
