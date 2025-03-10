package org.spring.ch5;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.spring.ch4.jdbcTemplate.DuplicateUserIdException;
import org.spring.ch5.v1.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.spring.ch5.v1.Level.*;

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
        userDao.deleteAll();

        User user1 = new User("user1", "영선", "pass123", 0, 0);
        User user2 = new User("user2", "서니", "pass010", 70, 0);
        User user3 = new User("user3", "선영", "pass323", 100, 40);
        userDao.add(user1);
        userDao.add(user2);
        userDao.add(user3);
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
                .isInstanceOf(DataAccessException.class)
                .isInstanceOf(EmptyResultDataAccessException.class)
                .hasMessageContaining("조회된 행이 없음");
    }

    @Test
    @Order(3)
    void testGetAll() {
        List<User> users = userDao.getAll();
        assertThat(users).hasSize(3);
    }

    @Test
    @Order(4)
    void testGetCount() {
        int count = userDao.getCount();
        assertThat(count).isEqualTo(3);
    }

    @Test
    @Order(5)
    void testSave() {
        User newUser = new User("user4", "lys", "pass789", 0, 20);
        userDao.add(newUser);

        User savedUser = userDao.getById("user4");
        assertThat(savedUser).isNotNull();
        assertThat(savedUser).isEqualTo(newUser);
    }

    @Test
    @Order(6)
    void testDelete() {
        userDao.deleteAll();
        assertThat(userDao.getCount()).isEqualTo(0);
    }

    @Test
    @Order(7)
    void duplicateId() {
        User user1 = new User("id", "name", "password", 51, 20);
        User user2 = new User("id", "name2", "password2", 51, 51);
        userDao.add(user1);
        assertThatThrownBy(() -> userDao.add(user2))
                .isInstanceOf(DuplicateUserIdException.class);
    }

    @Test
    @Order(8)
    void duplicateIdRoot() {
        User user = new User("id", "name", "password", 30, 30);
        try {
            userDao.add(user);
        } catch (DuplicateUserIdException e) {
            SQLException sqlException = (SQLException) e.getRootCause();
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(Objects.requireNonNull(this.jdbcTemplate.getDataSource()));
            // root Exception 이 SQLException 에서 직접 DuplicateKeyException 으로 전환하는 기능을 테스트
            assertThat(set.translate(null, null, sqlException)).isInstanceOf(DuplicateKeyException.class);
        }
    }

    @Test
    @Order(9)
    void update(){
        userDao.deleteAll();

        // given
        // fixture 오브젝트 : 테스트에서 일관된 환경을 제공하기 위해 미리 준비된 객체
        //                 테스트가 항상 같은 조건에서 실행되도록 보장하는 역할을 한다.
        User user = new User("user1", "영선", "pass123", 0, 0);
        User user2 = new User("user2", "영선", "pass123", 0, 0);
        userDao.add(user);
        userDao.add(user2);

        // when
        user.setName("서니");
        user.setPassword("pass010323");
        user.setLevel(SILVER);
        user.setLogin(77);
        user.setRecommend(11);

        int updatedRows = userDao.update(user);
        assertThat(updatedRows).isEqualTo(1);

        // then
        User updatedUser = userDao.getById(user.getId());
        assertThat(user).isEqualTo(updatedUser);
    }
}

