package org.spring.ch3.v8;

import org.spring.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Client 역할 수행
 */
public class UserDaoV8 {
    private final JdbcContext jdbcContext;

    public UserDaoV8(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void deleteAll() {
        // 익명 내부 클래스 사용 (인터페이스를 통해 선언)
        StatementStrategy deleteAllStrategy = new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection connection) throws SQLException {
                return connection.prepareStatement("delete from users");
            }
        };
        jdbcContext.workWithStatementStrategy(deleteAllStrategy);
    }

    public void add(final User user) {
        // 익명 내부 클래스 사용 (람다 통해 선언)
        StatementStrategy addStatementStrategy = connection -> {
            PreparedStatement pstmt = connection.prepareStatement("insert into users(id, name, password) values (?, ?, ?)");
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());

            return pstmt;
        };
        jdbcContext.workWithStatementStrategy(addStatementStrategy);
    }

    public int getCount() {
        // 익명 내부 클래스 사용 (람다 통해 선언)
        StatementStrategy getCountStatementStrategy = connection ->
                connection.prepareStatement("select count(*) from users");
        return jdbcContext.lookupWorkWithStatementStrategy(getCountStatementStrategy);
    }
}
