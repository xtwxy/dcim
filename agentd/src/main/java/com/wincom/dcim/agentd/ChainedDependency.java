package com.wincom.dcim.agentd;

import javax.annotation.Nonnull;

/**
 * A dependency <code>Runnable</code> that depends on a specific target
 * <code>Runnable</code>.
 *
 * @author master
 */
public class ChainedDependency extends Dependency {

    Dependency target;
    
    public ChainedDependency() {
        this.target = new Dependency();
    }

    public ChainedDependency(@Nonnull Dependency target) {
        this.target = target;
    }

    @Override
    public void onComplete() {
        target.run();
    }

    @Override
    public void onTimeout() {
        target.onTimeout();
    }

    @Override
    public void onError(Exception e) {
        target.onError(e);
    }

    @Override
    public void onClose() {
        target.onClose();
    }

    @Override
    public void run() {
        target.onComplete();
    }

}
