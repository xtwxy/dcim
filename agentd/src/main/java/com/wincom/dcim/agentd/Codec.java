package com.wincom.dcim.agentd;

import com.wincom.dcim.agentd.statemachine.StateMachine;

public interface Codec extends Target {
    public Object encode(Object msg);
    public Object decode(Object msg);
    
    public static class Adapter implements Codec {
        
        @Override
        public Object encode(Object msg) {
            return msg;
        }

        @Override
        public Object decode(Object msg) {
            return msg;
        }

        @Override
        public StateMachine withDependencies(StateMachine sm) {
            return sm;
        }
        
    }
}
