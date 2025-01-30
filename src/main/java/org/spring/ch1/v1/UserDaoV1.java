package org.spring.ch1.v1;

import org.spring.User;

import java.sql.*;


/**
 * v1. 초난감 UserDao
 *  중복되는 코드 많음
 *  유지보수와 변경에 취약
 */
public class UserDaoV1 {
    public void add (User user) throws Exception {
        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/toby", "sa", "");
        PreparedStatement pstmt = connection.prepareStatement("insert into users(id, name, password) values(?,?,?)");
        pstmt.setString(1, user.getId());
        pstmt.setString(2, user.getName());
        pstmt.setString(3, user.getPassword());
        pstmt.executeUpdate();

        pstmt.close();
        connection.close();
    }

    public User get (String id) throws Exception {
        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/toby", "sa", "");
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
}

