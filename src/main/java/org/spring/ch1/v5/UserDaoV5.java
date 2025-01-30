package org.spring.ch1.v5;

import org.spring.User;
import org.spring.ch1.v4.ConnectionMaker;
import org.spring.ch1.v4.NConnectionMaker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 * v5. UserDao 클라이언트 객체에게
 *      ConnectionMaker 구현체를 결정하고 관계를 맺는 관심사를 나눠준다. (관심사 분리)
 */

public class UserDaoV5 {

    private final ConnectionMaker connectionMaker;

    public UserDaoV5(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
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


