package org.spring.ch6.transaction;

import org.spring.ch6.transaction.proxyFactoryBean.TransactionAdvice;
import org.spring.ch6.transaction.userService.UserServiceImpl;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "org.spring.ch6.transaction")
@PropertySource({"classpath:ch5/application.properties"})
public class AppConfig {
    // v1. JDK의 다이내믹 프록시 생성
//    @Bean
//    public UserService userService(){
//        return new UserServiceTx(transactionManager(), userServiceImpl());
//    }

    // v2. 팩토리 빈을 사용하여 다이내믹 프록시를 빈으로 등록하는 과정
//    @Bean
//    public TxProxyFactoryBean userService() {
//        Object target = userServiceImpl();
//        PlatformTransactionManager transactionManager = transactionManager();
//        String pattern = "upgradeLevel";
//        Class<?> serviceInterface = UserService.class;
//
//        return new TxProxyFactoryBean(target, transactionManager, pattern, serviceInterface);
//    }

    // v3. 스프링의 ProxyFactoryBean 적용하기 => 어노테이션으로 빈 등록했기 때문에 여기서는 주석처리함
    @Bean
    public ProxyFactoryBean userService() {
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(userServiceImpl());
        proxyFactoryBean.setInterceptorNames("transactionAdvisor");     // 이 메서드를 사용하면 어드바이스와 어드바이저를 모두 설정(추가)할 수 있다. 빈의 아이디를 String으로 나열하면 된다.
        return proxyFactoryBean;
    }

    // 포인트컷을 빈으로 등록
    @Bean
    public NameMatchMethodPointcut transactionPointcut() {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("upgrade*");
        return pointcut;
    }

    // 어드바이저를 빈으로 등록 (어드바이스오 포인트컷은 어드바이저의 생성자로 넣어도 되고, set을 통해 DI 해도 된다.)
    @Bean
    public DefaultPointcutAdvisor transactionAdvisor(TransactionAdvice transactionAdvice) {
        DefaultPointcutAdvisor pointcutAdvisor = new DefaultPointcutAdvisor();
        pointcutAdvisor.setAdvice(transactionAdvice);
        pointcutAdvisor.setPointcut(transactionPointcut());
        return pointcutAdvisor;
    }


    @Bean
    public UserServiceImpl userServiceImpl() {
        return new UserServiceImpl(userDao(), mailSender());
    }

    @Bean
    public MailSender mailSender() {
        return new JavaMailSenderImpl();
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
