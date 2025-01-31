package org.spring.ch1.v6;

import org.spring.User;
import org.spring.ch1.v4.ConnectionMaker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * v6. UserDaoTest 클래스에 기능 테스트 + 객체 생성 및 관계 정의
 *      이렇게 2개의 관심사가 몰렸다. 이를 분리해주자!
 */
public class UserDaoV6 {

    private final ConnectionMaker connectionMaker;

    public UserDaoV6(ConnectionMaker connectionMaker) {
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


