package com.wincom.dcim.agentd;

/**
 *
 * @author master
 */
public interface Dependable {
    public Dependency withDependencies(Dependency r);
}
