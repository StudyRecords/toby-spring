package org.spring.ch5.v4;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "org.spring.ch5.v4")
public class AppConfigV4 {

    @Bean
    public UserServiceV4 userService() {
        return new UserServiceV4(userDao());
    }

    @Bean
    public UserDaoV4 userDao() {
        return new UserDaoV3JdbcV4(jdbcTemplate());
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource()); // 트랜잭션 매니저 등록
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:tcp://localhost/~/toby"); // H2 TCP 서버 모드
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }
}
