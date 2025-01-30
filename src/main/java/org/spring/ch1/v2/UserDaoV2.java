package org.spring.ch1.v2;

import org.spring.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;


/**
 * 메서드 추출을 통해 관심사 분리
 */

public class UserDaoV2 {
    public void add (User user) throws Exception {
        Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement("insert into users(id, name, password) values(?,?,?)");
        pstmt.setString(1, user.getId());
        pstmt.setString(2, user.getName());
        pstmt.setString(3, user.getPassword());
        pstmt.executeUpdate();

        pstmt.close();
        connection.close();
    }

    public User get (String id) throws Exception {
        Connection connection = getConnection();
        PreparedStatement pstmt = connection.prepareStatement("select * from users where id = ?");
        pstmt.setString(1, id);

        ResultSet rs = pstmt.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        pstmt.close();
        connection.close();

        return user;
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/toby", "sa", "");
        return connection;
    }
}


