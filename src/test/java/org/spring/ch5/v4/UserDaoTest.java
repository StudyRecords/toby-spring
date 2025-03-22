package org.spring.ch5.v4;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.spring.ch4.jdbcTemplate.DuplicateUserIdException;
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
import static org.spring.ch5.v4.LevelV4.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)            // 각 테스트 메서드간 테스트 인스턴스 공유
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)      // 테스트 실행 순서 지정 가능
@Transactional               // 테스트 후 자동 롤백
public class UserDaoTest {

    private UserDaoV4 userDao;
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    void setUp() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfigV4.class);
        this.userDao = ac.getBean("userDao", UserDaoV4.class);
        this.jdbcTemplate = ac.getBean("jdbcTemplate", JdbcTemplate.class);
        userDao.deleteAll();

        UserV4 user1 = new UserV4("user1", "영선", "pass123", BASIC, 0, 0);
        UserV4 user2 = new UserV4("user2", "서니", "pass010", SILVER, 70, 0);
        UserV4 user3 = new UserV4("user3", "선영", "pass323", GOLD, 100, 40);
        userDao.add(user1);
        userDao.add(user2);
        userDao.add(user3);
    }

    @Test
    @Order(1)
    void testGetById_Found() {
        UserV4 user = userDao.getById("user1");
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
        List<UserV4> users = userDao.getAll();
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
        UserV4 newUser = new UserV4("user4", "lys", "pass789", BASIC, 0, 20);
        userDao.add(newUser);

        UserV4 savedUser = userDao.getById("user4");
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
        UserV4 user1 = new UserV4("id", "name", "password", SILVER, 51, 20);
        UserV4 user2 = new UserV4("id", "name2", "password2", GOLD, 51, 51);
        userDao.add(user1);
        assertThatThrownBy(() -> userDao.add(user2))
                .isInstanceOf(DuplicateUserIdException.class);
    }

    @Test
    @Order(8)
    void duplicateIdRoot() {
        UserV4 user = new UserV4("id", "name", "password", BASIC, 30, 30);
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
    void update() {
        userDao.deleteAll();

        // given
        // fixture 오브젝트 : 테스트에서 일관된 환경을 제공하기 위해 미리 준비된 객체
        //                 테스트가 항상 같은 조건에서 실행되도록 보장하는 역할을 한다.
        UserV4 user = new UserV4("user1", "영선", "pass123", BASIC, 0, 0);
        UserV4 user2 = new UserV4("user2", "영선", "pass123", BASIC, 0, 0);
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
        UserV4 updatedUser = userDao.getById(user.getId());
        assertThat(user).isEqualTo(updatedUser);
    }
}

