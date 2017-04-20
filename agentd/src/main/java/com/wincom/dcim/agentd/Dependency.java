package com.wincom.dcim.agentd;

/**
 *
 * @author master
 */
public interface Dependency {
    public Runnable withDependencies(Runnable r);
}
