package com.wincom.driver.dds3366d.internal.primitives;

/**
 * Created by master on 5/4/17.
 */
public class StateBuilder {
    private StateBuilder parent;
    private StateBuilder success;
    private StateBuilder fail;

    private State current;

    private StateBuilder() {

    }

    private StateBuilder(StateBuilder prev) {
        this.parent = prev;
    }

    public StateBuilder initial() {
        return new StateBuilder();
    }
    public StateBuilder success() {
        if(this.current == null) {
            return this;
        } else {
            this.success = new StateBuilder(this);
            return this.success;
        }
    }
    public StateBuilder state(State s) {
        this.current = s;
        return this;
    }
    public StateBuilder fail() {
        if(this.current == null) {
            return this;
        } else {
            this.fail = new StateBuilder(this);
            return this.fail;
        }
    }
    public State build() {
        StateBuilder head = this;
        while(head.parent != null) {
            head = head.parent;
        }

        return traverse(head);
    }

    private State traverse(StateBuilder builder) {
        if(this.current == null) {
            return stopState();
        }
        if(builder.success != null) {
            current.success(traverse(builder.success));
        }
        if(builder.fail != null) {
            current.fail(traverse(builder.fail));
        }
        return this.current;
    }

    private State traverse0(StateBuilder builder) {
        StateBuilder node = builder;

        while(true) {
            if(node.success != null) {
                node.current.success(node.success.current);
                node = node.success;
                continue;
            }
            while (node.parent != null && node.parent.fail == null) {
                node = node.parent;
            }
            if(node.parent == null) {
                // root node.
                break;
            } else {
                // none root node, node.parent.fail != null
                // check if current node is the node.parent.fail
                // if it is, upper one level
                // else switching current node to node.parent.fail
                while (node.parent != null && node == node.parent.fail) {
                    node = node.parent;
                }
                if(node.parent == null) {
                    break;
                } else {
                    node.parent.current.fail(node.parent.fail.current);
                    node = node.parent.fail;
                }
            }
        }
        return builder.current;
    }

    private State stopState() {
        return new State.Adapter();
    }

}
