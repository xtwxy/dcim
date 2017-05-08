package com.wincom.dcim.agentd;

import com.wincom.dcim.agentd.statemachine.StateMachine;

/**
 *
 * @author master
 */
public interface Target {
    public StateMachine withDependencies(StateMachine sm);
}
