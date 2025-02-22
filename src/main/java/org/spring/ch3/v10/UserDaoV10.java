package org.spring.ch3.v10;

import org.spring.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * v10. 콜백 재활용 적용!
 *      익명 내부 클래스 내에서 자주 변경되는 코드 vs 고정된 코드 분리 -> 복잡도 낮추기!!
 */
public class UserDaoV10 {
    private final JdbcContext jdbcContext;

    public UserDaoV10(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void deleteAll() {
        jdbcContext.executeSql("delete from users");
    }

    public int getCount() {
        return jdbcContext.executeSqlWithResultSet("select count(*) from users");
    }

    public void add(final User user) {
        jdbcContext.executeUpdate("insert into users(id, name, password) values (?, ?, ?)",
                user.getId(), user.getName(), user.getPassword());
    }

}
