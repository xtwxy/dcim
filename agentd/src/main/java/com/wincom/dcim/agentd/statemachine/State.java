package com.wincom.dcim.agentd.statemachine;

import com.wincom.dcim.agentd.HandlerContext;
import com.wincom.dcim.agentd.messages.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * State of State Machine.
 *
 * Created by master on 5/4/17.
 */
public interface State {

    /**
     * Life cycle event handler for enter this state.
     *
     * For each state, this may only be called once.
     *
     * @param ctx
     * @return
     */
     State enter(HandlerContext ctx);

    /**
     * Life cycle event handler for exit this state.
     *
     * For each state, this may only be called once.
     *
     * @param ctx
     */
     void exit(HandlerContext ctx);

    /**
     * Accept incoming <code>Message</code>.
     *
     * @param ctx
     * @param m
     * @return
     */
     State on(HandlerContext ctx, Message m);

    /**
     * Test if it is stopped state.
     *
     * @return
     */
     boolean stopped();

    /**
     * Set the next <code>State</code> for after current state is completed
     * successfully.
     *
     * @param s
     * @return
     */
     State success(State s);

    /**
     * Get the next <code>State</code> for after current state is completed
     * successfully.
     *
     * @return
     */
     State success();

    /**
     * Set the next <code>State</code> for after current state is completed with
     * error.
     *
     * @param s
     * @return
     */
     State error(State s);

    /**
     * Get the next <code>State</code> for after current state is completed with
     * error.
     *
     * @return
     */
     State error();

    /**
     * Set the next <code>State</code> for after current state is completed with
     * failure.
     *
     * @param s
     * @return
     */
     State failure(State s);

    /**
     * Get the next <code>State</code> for after current state is completed with
     * failure.
     *
     * @return
     */
     State failure();

    class Adapter implements State {

        protected final Logger log = LoggerFactory.getLogger(this.getClass());

        private State success;
        private State failure;
        private State error;

        public Adapter() {
        }

        public Adapter(State success, State fail) {
            this.success = success;
            this.error = fail;
        }

        @Override
        public State on(HandlerContext ctx, Message m) {
            return this;
        }

        @Override
        public boolean stopped() {
            return success == null;
        }

        @Override
        public State success(State s) {
            this.success = s;
            return this;
        }

        @Override
        public State error(State s) {
            this.error = s;
            return this;
        }

        @Override
        public State error() {
            if (this.error != null) {
                return this.error;
            } else if (this.success != this && this.success != null) {
                return this.success.error();
            } else {
                return null;
            }
        }

        @Override
        public State success() {
            return this.success;
        }

        @Override
        public State failure(State s) {
            this.failure = s;
            return this;
        }

        @Override
        public State failure() {
            if (this.failure != null) {
                return this.failure;
            } else if (this.success != this && this.success != null) {
                return this.success.failure();
            } else {
                return null;
            }
        }

        @Override
        public State enter(HandlerContext ctx) {
            return this;
        }

        @Override
        public void exit(HandlerContext ctx) {
        }

        @Override
        public String toString() {
            return String.format("%s@%s", getClass().getSimpleName(), hashCode());
        }
    }

    class Stop extends Adapter {

        @Override
        public State enter(HandlerContext ctx) {
            ctx.onRequestCompleted();
            return this;
        }

        @Override
        public State on(HandlerContext ctx, Message m) {
            log.info(String.format("stop state: on(%s, %s)", ctx, m));
            return this;
        }

        @Override
        public boolean stopped() {
            return true;
        }
    }
}
