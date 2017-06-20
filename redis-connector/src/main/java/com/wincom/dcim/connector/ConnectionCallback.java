package com.wincom.dcim.connector;

/**
 * Created by master on 6/20/17.
 */
public interface ConnectionCallback {
    public void completed(Throwable cause, Connection c);
}
