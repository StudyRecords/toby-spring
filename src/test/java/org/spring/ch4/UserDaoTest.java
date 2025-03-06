package org.spring.ch4;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.spring.User;
import org.spring.ch4.independentDao.UserDao;
import org.spring.ch4.jdbcTemplate.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)            // 각 테스트 메서드간 테스트 인스턴스 공유
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)      // 테스트 실행 순서 지정 가능
@Transactional               // 테스트 후 자동 롤백
public class UserDaoTest {

    private UserDao userDao;
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    void setUp() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        this.userDao = ac.getBean("userDao", UserDao.class);
        this.jdbcTemplate = ac.getBean("jdbcTemplate", JdbcTemplate.class);

        jdbcTemplate.execute("INSERT INTO users (id, name, password) VALUES ('user1', '영선', 'pass123')");
        jdbcTemplate.execute("INSERT INTO users (id, name, password) VALUES ('user2', '서니', 'pass456')");
    }

    @Test
    @Order(1)
    void testGetById_Found() {
        User user = userDao.getById("user1");
        Assertions.assertThat(user.getName()).isEqualTo("영선");
    }

    @Test
    @Order(2)
    void testGetById_NotFound() {
        assertThatThrownBy(() -> userDao.getById("lkanvlk"))
                .isInstanceOf(EmptyResultDataAccessException.class)
                .hasMessageContaining("조회된 행이 없음");
    }

    @Test
    @Order(3)
    void testGetAll() {
        List<User> users = userDao.getAll();
        assertThat(users).hasSize(2);
    }

    @Test
    @Order(4)
    void testGetCount() {
        int count = userDao.getCount();
        assertThat(count).isEqualTo(2);
    }

    @Test
    @Order(5)
    void testSave() {
        User newUser = new User("user3", "lys", "pass789");
        userDao.add(newUser);

        User savedUser = userDao.getById("user3");
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("lys");
    }

    @Test
    @Order(6)
    void testDelete() {
        userDao.deleteAll();
        assertThat(userDao.getCount()).isEqualTo(0);
    }
}

