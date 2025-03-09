package org.spring.ch3.v9;

import org.spring.ch4.User;
import org.spring.ch3.dataSource.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * v9. JdbcContext 수동 생성, 수동 DI
 * => UserDaoV9와 DataSource만 스프링 빈이고, JdbcContext는 스프링 빈이 아니다.
 */
public class UserDaoV9 {
    /**
     * 생성자 주입
     */
    private final DataSource dataSource;          // 스프링 빈이 자동 주입해줌
    private final JdbcContext jdbcContext;        // 수동으로 생성 (이 때 DataSource를 수동으로 주입해줌)

    public UserDaoV9(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcContext = new JdbcContext(dataSource);
    }

    /**
     * 수정자 주입
     * 모든 면에서 생성자 주입이 더 나은 것 같은데...
     * Q. 수정자 주입은... 실행 중에 DataSource 구현체를 변경할 수 있다는 점 때문에 사용하는건가?
     * A. xml을 사용하여 빈을 관리할 때에는 수정자 주입을 주로 사용해서 그럼 (1장 더 읽고 정리하기)
     */

//    // DI 컨테이너가 DataSource 오브젝트를 주입해줄 때 호출된다. (JdbcContext 수동 DI 작업 수행)
//    public void setDataSource(DataSource dataSource){
//        this.jdbcContext = new JdbcContext();           // jdbcContext 객체 생성 (IoC)
//        this.jdbcContext.setDataSource(dataSource);     // 의존 오브젝트인 DataSource를 JdbcContext에 주입
//    }
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
