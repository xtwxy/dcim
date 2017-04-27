package com.wincom.dcim.agentd;

/**
 *
 * @author master
 */
public interface IoCompletionHandler {

    public void onTimeout();

    public void onError(Exception e);

    public void onClose();

    public void onComplete();

    public static class Adapter implements IoCompletionHandler {

        protected IoCompletionHandler handler;

        public Adapter(IoCompletionHandler handler) {
            this.handler = handler;
        }

        @Override
        public void onTimeout() {
            handler.onTimeout();
        }

        @Override
        public void onError(Exception e) {
            handler.onError(e);
        }

        @Override
        public void onClose() {
            handler.onClose();
        }

        @Override
        public void onComplete() {
            handler.onComplete();
        }

    }
}
