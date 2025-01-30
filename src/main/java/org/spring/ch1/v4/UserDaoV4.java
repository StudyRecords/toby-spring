package org.spring.ch1.v4;

import org.spring.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 * v4. 합성과 인터페이스를 통해 관심사 분리
 */

public class UserDaoV4 {

    private final ConnectionMaker connectionMaker;

    public UserDaoV4() {
        this.connectionMaker = new NConnectionMaker();
    }

    public void add(User user) throws Exception {
        Connection connection = connectionMaker.makeConnection();
        PreparedStatement pstmt = connection.prepareStatement("insert into users(id, name, password) values(?,?,?)");
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


