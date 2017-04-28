package com.wincom.dcim.agentd;

/**
 * A dependency <code>Runnable</code> target that depends on no target
 * <code>Runnable</code>.
 *
 * @author master
 */
public class Dependency 
        implements IoCompletionHandler, Runnable {

    public Dependency() {
    }

    @Override
    public void onComplete() {
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
    public void run() {
    }

}
