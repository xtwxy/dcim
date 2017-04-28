package com.wincom.dcim.agentd;

import io.netty.channel.Channel;

public interface Connector extends IoCompletionHandler {
    public void onConnect(Channel ch);
    public static class Adapter implements Connector {

        @Override
        public void onConnect(Channel ch) {
            
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
