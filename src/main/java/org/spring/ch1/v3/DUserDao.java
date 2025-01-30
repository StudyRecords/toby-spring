package org.spring.ch1.v3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DUserDao extends UserDaoV3{
    @Override
    protected Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/toby", "sa", "");
        return connection;
    }
}
