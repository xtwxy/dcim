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

    public static class ChainedAdapter implements IoCompletionHandler {

        protected IoCompletionHandler handler;

        public ChainedAdapter(IoCompletionHandler handler) {
            if(handler != null) {
                this.handler = handler;
            } else {
                this.handler = new Adapter();
            }
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
    public static class Adapter implements IoCompletionHandler {

        public Adapter() {
        }

        @Override
        public void onTimeout() {
        }

        @Override
        public void onError(Exception e) {
        }

        @Override
        public void onClose() {
        }

        @Override
        public void onComplete() {
        }

    }
}
