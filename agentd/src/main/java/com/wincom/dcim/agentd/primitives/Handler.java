package com.wincom.dcim.agentd.primitives;

/**
 *
 * @author master
 * @param <T>
 */
public interface Handler<T> {
    public void apply(T t);
}
