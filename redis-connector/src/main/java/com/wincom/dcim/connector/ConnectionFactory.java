package com.wincom.dcim.connector;

public interface ConnectionFactory {
    public void getConnection(ConnectionCallback cc);
    public void release(Connection c);
}

