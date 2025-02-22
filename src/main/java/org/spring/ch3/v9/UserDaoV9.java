package org.spring.ch3.v9;

import org.spring.User;
import org.spring.ch3.dataSource.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * JdbcContext 수동 생성, 수동 DI
 * => UserDaoV9와 DataSource만 스프링 빈이고, JdbcContext는 스프링 빈이 아니다.
 */
public class UserDaoV9 {
    /**
     * 생성자 주입
     */
//    private final JdbcContext jdbcContext;
//    public UserDaoV9(DataSource dataSource) {
//        this.jdbcContext = new JdbcContext(dataSource);
//    }

    /**
     * 수정자 주입
     * 모든 면에서 생성자 주입이 더 나은 것 같은데...
     * 수정자 주입은... 실행 중에 DataSource 구현체를 변경할 수 있다는 점 때문에 사용하는건가?
     */
    private JdbcContext jdbcContext;
    private DataSource dataSource;          // 굳이 필드에 저장해야 하나?

    // DI 컨테이너가 DataSource 오브젝트를 주입해줄 때 호출된다. (JdbcContext 수동 DI 작업 수행)
    public void setDataSource(DataSource dataSource){
        this.jdbcContext = new JdbcContext();           // jdbcContext 객체 생성 (IoC)
        this.jdbcContext.setDataSource(dataSource);     // 의존 오브젝트인 DataSource를 JdbcContext에 주입
        this.dataSource = dataSource;           // 아직 JdbcContext를 적용하지 않는 메서드를 위해 저장해둔다...??
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
