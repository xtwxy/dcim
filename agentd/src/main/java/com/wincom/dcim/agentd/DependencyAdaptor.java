package com.wincom.dcim.agentd;

/**
 * A dependency <code>Runnable</code> of a specific target
 * <code>Runnable</code>.
 *
 * @author master
 */
public abstract class DependencyAdaptor implements Runnable {
    protected Runnable target;
    public DependencyAdaptor(Runnable target) {
        this.target = target;
    }
}
