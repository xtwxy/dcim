package com.wincom.dcim.connector;

public interface ConnectionFactory {
    void getConnection(ConnectionCallback cc);
    void release(Connection c);
}

