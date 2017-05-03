package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public interface Message {

    public void apply(Handler handler);

    public static class Adapter implements Message {

        @Override
        public void apply(Handler handler) {
            handler.handle(this);
        }

    }
}
