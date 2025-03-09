package org.spring.ch3.v10;

import org.spring.ch4.User;

/**
 * v10. 콜백 재활용 적용!
 * 익명 내부 클래스 내에서 자주 변경되는 코드 vs 고정된 코드 분리 -> 복잡도 낮추기!!
 * 클라이언트에서 콜백 오브젝트를 생성하지 않고 JdbcContext에서 생성함으로써 코드의 중복을 최소화함
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
