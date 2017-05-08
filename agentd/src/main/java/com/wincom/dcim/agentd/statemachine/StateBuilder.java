package com.wincom.dcim.agentd.statemachine;

/**
 * Created by master on 5/4/17.
 */
public class StateBuilder {

    private StateBuilder parent;
    private StateBuilder success;
    private StateBuilder fail;

    private State current;
    private State stop;

    private StateBuilder() {
        stop = new State.Adapter();
    }

    private StateBuilder(State s) {
        this();
        this.current = s;
    }

    private StateBuilder(StateBuilder prev) {
        this.parent = prev;
        this.stop = prev.stop;
    }

    public static StateBuilder initial() {
        return new StateBuilder();
    }

    public static StateBuilder initial(State s) {
        return new StateBuilder(s);
    }

    public StateBuilder success() {
        this.success = new StateBuilder(this);
        return this.success;
    }

    public StateBuilder state(State s) {
        this.current = s;
        return this;
    }

    /**
     * Get a state builder for fail state.
     *
     * @return
     */
    public StateBuilder fail() {
        this.fail = new StateBuilder(this);
        return this.fail;
    }

    public State build() {
        StateBuilder head = this;
        while (head.parent != null) {
            head = head.parent;
        }
        if (head.current == null) {
            return stopState();
        }

        return traverse0(head);
    }

    /**
     * Traverse through all nodes to build <code>State</code> tree.
     *
     * @param builder
     * @return
     */
    private static State traverse(StateBuilder builder) {
        if (builder.success != null) {
            builder.current.success(traverse(builder.success));
        } else {
            builder.current.success(builder.stop);
        }
        if (builder.fail != null) {
            builder.current.fail(traverse(builder.fail));
        } else {
            builder.current.fail(builder.stop);
        }
        return builder.current;
    }

    /**
     * A non-recursive traverse routine.
     *
     * @param builder
     * @return
     */
    private static State traverse0(StateBuilder builder) {
        StateBuilder node = builder;
        boolean backtrack = false;

        while (true) {
            if (!backtrack) {
                processNode(node);
                if (node.success != null) {
                    node = node.success;
                } else if (node.fail != null) {
                    node = node.fail;
                } else {
                    // is leaf
                    backtrack = true;
                }
            }
            if (backtrack) {
                if (node.parent != null) {
                    if (node.parent.fail != null && node.parent.fail != node) {
                        node = node.parent.fail;

                        backtrack = false;
                    } else {
                        // continue backtrack...
                        node = node.parent;
                    }
                } else {
                    // reach the root, stop
                    break;
                }

            }
        }
        return builder.current;
    }

    private static void processNode(StateBuilder node) {
        if (node.success != null) {
            node.current.success(node.success.current);
        }else{
            node.current.success(node.stop);
        }
        if (node.fail != null) {
            node.current.fail(node.fail.current);
        }else{
            node.current.fail(node.stop);
        }
    }

    public State stopState() {
        return stop;
    }

}
