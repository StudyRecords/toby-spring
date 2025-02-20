package org.spring.ch3.v1;

import org.spring.ch3.dataSource.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 * v1. 초난감 UserDao
 * - 예외 처리 X
 */
public class UserDaoV1 {

    private final DataSource dataSource;

    public UserDaoV1(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void deleteAll() throws Exception {
        Connection connection = dataSource.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("delete from users");
        pstmt.executeUpdate();

        pstmt.close();
        connection.close();
    }

    public int getCount() throws Exception {
        Connection connection = dataSource.getConnection();
        PreparedStatement pstmt = connection.prepareStatement("select count(*) from users");
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        pstmt.close();
        connection.close();

        return count;
    }
}

