package org.spring.ch1.dbConnection;

import org.spring.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDao {

    private final ConnectionMaker connectionMaker;

    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add(User user) throws Exception {
        Connection connection = connectionMaker.makeConnection();
        PreparedStatement pstmt = connection.prepareStatement("insert into users (id, name, password) values(?,?,?)");
        pstmt.setString(1, user.getId());
        pstmt.setString(2, user.getName());
        pstmt.setString(3, user.getPassword());
        pstmt.executeUpdate();

        pstmt.close();
        connection.close();
    }

    public User get(String id) throws Exception {
        Connection connection = connectionMaker.makeConnection();
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
