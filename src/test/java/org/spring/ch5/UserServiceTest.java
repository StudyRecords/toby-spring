package org.spring.ch5;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.spring.ch5.v1.AppConfig;
import org.spring.ch5.v1.User;
import org.spring.ch5.v1.UserDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.spring.ch5.v1.Level.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)            // 각 테스트 메서드간 테스트 인스턴스 공유
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)      // 테스트 실행 순서 지정 가능
@Transactional               // 테스트 후 자동 롤백
public class UserServiceTest {

    private UserDao userDao;
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    void setUp() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        this.userDao = ac.getBean("userDao", UserDao.class);
        this.jdbcTemplate = ac.getBean("jdbcTemplate", JdbcTemplate.class);
        userDao.deleteAll();

        User user1 = new User("user1", "영선", "pass123", BASIC, 0, 0);
        User user2 = new User("user2", "서니", "pass010", SILVER, 70, 0);
        User user3 = new User("user3", "선영", "pass323", GOLD, 100, 40);
        userDao.add(user1);
        userDao.add(user2);
        userDao.add(user3);
    }

    @Test
    public void bean(){
        assertThat(userDao).isNotNull();
    }
}
