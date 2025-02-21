package org.spring.ch3.v6.strategy;

import org.spring.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddStatementStrategy implements StatementStrategy {

    private final User user;

    public AddStatementStrategy(User user) {
        this.user = user;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("insert into users(id, name, password) values (?, ?, ?)");
        pstmt.setString(1, user.getId());
        pstmt.setString(2, user.getName());
        pstmt.setString(3, user.getPassword());

        return pstmt;
    }
}
