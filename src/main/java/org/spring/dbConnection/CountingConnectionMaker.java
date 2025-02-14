package org.spring.dbConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class CountingConnectionMaker implements ConnectionMaker{
    private final ConnectionMaker realConnectionMaker;
    private int cnt = 0;

    public CountingConnectionMaker(ConnectionMaker connectionMaker){
        this.realConnectionMaker = connectionMaker;
    }

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        cnt++;
        return realConnectionMaker.makeConnection();
    }

    public int getCount(){
        return cnt;
    }
}
