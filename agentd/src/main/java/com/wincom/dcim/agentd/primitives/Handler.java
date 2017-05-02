package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 */
public interface Handler {

    public void handleGetSignalValuesRequest(GetSignalValues.Request r);

    public void handleGetSignalValuesRequest(GetSignalValues.Response r);

    public void handleSetSignalValuesRequest(SetSignalValues.Request r);

    public void handleSetSignalValuesResponse(SetSignalValues.Response r);

    public void handlePushEvents(PushEvents pe);

    public void apply(Message m);

    public static class Adapter implements Handler {

        @Override
        public void handleGetSignalValuesRequest(GetSignalValues.Request r) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void handleGetSignalValuesRequest(GetSignalValues.Response r) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void handleSetSignalValuesRequest(SetSignalValues.Request r) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void handleSetSignalValuesResponse(SetSignalValues.Response r) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void handlePushEvents(PushEvents pe) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void apply(Message m) {
            m.apply(this);
        }

    }
}
