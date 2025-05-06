package org.spring.ch6.transaction;

import com.zaxxer.hikari.HikariDataSource;
import org.spring.ch6.proxyFactoryBean.TestUserServiceImpl;
import org.spring.ch6.transaction.userService.UserService;
import org.spring.ch6.transaction.userService.UserServiceImpl;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@ComponentScan(basePackages = "org.spring.ch6.transaction")
@PropertySource({"classpath:ch5/application.properties"})
@EnableTransactionManagement
public class TestConfig {

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
//        return new TxProxyFactoryBean(target, transactionManager, pattern, serviceInterface);
//    }


    // v3. 스프링의 ProxyFactoryBean 적용하기 => 어노테이션으로 빈 등록했기 때문에 여기서는 주석처리함
//    @Bean
//    public ProxyFactoryBean userService() {
//        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
//        proxyFactoryBean.setTarget(userServiceImpl());
//        proxyFactoryBean.setInterceptorNames("transactionAdvisor");     // 이 메서드를 사용하면 어드바이스와 어드바이저를 모두 설정(추가)할 수 있다. 빈의 아이디를 String으로 나열하면 된다.
//        return proxyFactoryBean;
//    }

    // 포인트컷을 빈으로 등록
//    @Bean
//    public NameMatchMethodPointcut transactionPointcut() {
//        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
//        pointcut.setMappedName("upgrade*");
//        return pointcut;
//    }

    // 어드바이저를 빈으로 등록 (어드바이스와 포인트컷은 어드바이저의 생성자로 넣어도 되고, set을 통해 DI 해도 된다.)
    @Bean
    public DefaultPointcutAdvisor transactionAdvisor() {
        DefaultPointcutAdvisor pointcutAdvisor = new DefaultPointcutAdvisor();
        pointcutAdvisor.setAdvice(transactionAdvice());
        pointcutAdvisor.setPointcut(transactionPointcut());
        return pointcutAdvisor;
    }

//    @Bean
//    public UserServiceImpl userServiceImpl() {
//        return new UserServiceImpl(userDao(), mailSender());
//    }

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
        dataSource.setUrl("jdbc:h2:tcp://localhost/~/toby");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    // v4. 빈후처리기 적용해서 프록시가 자동 생성 & 자동 빈등록 되게 만들기
    @Bean
    public DefaultAdvisorAutoProxyCreator autoProxyCreator() {
        // 사실 다른 빈에서 참조되거나 조회되지 않고 혼자서도 스스로 빈으로 잘 활동하기 때문에 id는 아무거나 해도 상관없다.
        return new DefaultAdvisorAutoProxyCreator();
    }

    // 포인트컷을 빈으로 등록하기
//    @Bean
//    public NameMatchMethodPointcut transactionPointcut() {
//        NameMatchClassMethodPointcut classMethodPointcut = new NameMatchClassMethodPointcut();
//        classMethodPointcut.setMappedClassName("*ServiceImpl");     // 클래스 이름 패턴
//        classMethodPointcut.setMappedName("upgrade*");      // 메서드 이름 패턴
//        return classMethodPointcut;
//    }

    // v4. 이제 프록시 객체가 동적으로 빈으로 자동등록되니까 프록시를 직접 빈으로 등록하지 않아도 된다.
    // 그러니까 타깃 오브젝트를 id="userService"로 설정해도 무관하다.
    @Bean
    public UserService userService() {
        return new UserServiceImpl(userDao(), mailSender());
    }

    // v4. 트랜잭션 롤백을 테스트하려면 얘를 빈으로 등록해야 한다.
    @Bean
    public UserService testUserService() {
        return new TestUserServiceImpl(userDao(), mailSender());
    }

    // v5. TransactionAdvice 대신 스프링에서 제공하는 TransactionInterceptor 사용하기
    public TransactionInterceptor transactionAdvice() {
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionManager(transactionManager());
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();

        RuleBasedTransactionAttribute readOnlyAttribute = new RuleBasedTransactionAttribute();
        readOnlyAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        readOnlyAttribute.setReadOnly(true);
        source.addTransactionalMethod("get*", readOnlyAttribute);

//        RuleBasedTransactionAttribute upgradeAttribute = new RuleBasedTransactionAttribute();
//        upgradeAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//        upgradeAttribute.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
//        source.addTransactionalMethod("upgrade*", upgradeAttribute);

        RuleBasedTransactionAttribute defaultAttribute = new RuleBasedTransactionAttribute();
        defaultAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        source.addTransactionalMethod("*", defaultAttribute);

        transactionInterceptor.setTransactionAttributeSource(source);

        return transactionInterceptor;
    }

    // v6. AspectJ 포인트컷 표현식으로 포인트컷 등록하기
    @Bean
    public AspectJExpressionPointcut transactionPointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("bean(*Service)");       // 이름이 Service로 끝나는 모든 빈에 트랜잭션 부가기능이 적용된다.
        return pointcut;
    }
}
