package org.spring.ch5.transaction;

import org.spring.ch5.transaction.mail.DummyMailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "org.spring.ch5.transaction")
@PropertySource({"classpath:ch5/application.properties"})
public class AppConfig {

    @Bean
    public UserService userService() {
        return new UserService(userDao(), transactionManager(), mailSender());
    }

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }

    @Bean
    public UserDao userDao() {
        return new UserDaoJdbc(jdbcTemplate());
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
