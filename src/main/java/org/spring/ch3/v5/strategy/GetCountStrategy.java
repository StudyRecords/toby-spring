package org.spring.ch3.v5.strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class GetCountStrategy implements StatementStrategy {
    @Override
    public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("select count(*) from users");
    }
}

