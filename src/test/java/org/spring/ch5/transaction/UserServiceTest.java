package org.spring.ch5.transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.spring.ch5.transaction.Level.*;

@ExtendWith(SpringExtension.class)                         // JUnit 5에서 Spring 테스트 확장 활성화
@ContextConfiguration(classes = AppConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)            // 각 테스트 메서드간 테스트 인스턴스 공유
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)      // 테스트 실행 순서 지정 가능
//@Transactional                                             // 테스트 후 자동 롤백
public class UserServiceTest {

    private static final Log log = LogFactory.getLog(UserServiceTest.class);
    private List<User> users;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;

    @BeforeEach
    void init() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/toby", "sa", "");
        userDao.deleteAll(connection);
    }

    @BeforeAll
    void setUp() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/toby", "sa", "");
        userDao.deleteAll(connection);
        users = Arrays.asList(new User("user1", "영선", "pass123", BASIC, 0, 30),
                new User("user2", "서니", "pass010", BASIC, 50, 29),
                new User("user3", "선영", "pass323", SILVER, 50, 31),
                new User("user4", "이영선", "pass121", SILVER, 50, 40),
                new User("user5", "이영", "pass212", GOLD, 51, 30));
    }

    @Test
    @Order(1)
    public void bean() {
        assertThat(userDao).isNotNull();
        assertThat(userService).isNotNull();
    }

    @Test
    public void upgradeLevels() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/toby", "sa", "");
        users.forEach(user -> userDao.add(connection, user));
        userService.upgradeLevels();

        checkLevel(users.get(0), BASIC);
        checkLevel(users.get(1), SILVER);
        checkLevel(users.get(2), GOLD);
        checkLevel(users.get(3), GOLD);
        checkLevel(users.get(4), GOLD);
    }

    private void checkLevel(User user, Level level) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/toby", "sa", "");
        User updatedUser = userDao.getById(connection, user.getId());
        assertThat(updatedUser.getLevel()).isEqualTo(level);
    }

    @Test
    @Order(3)
    public void add() throws SQLException {
        User goldUser = users.get(4);
        User silverUser = users.get(2);

        userService.add(goldUser);
        userService.add(silverUser);

        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/toby", "sa", "");
        User savedGoldUser = userDao.getById(connection, goldUser.getId());
        User savedSilverUser = userDao.getById(connection, silverUser.getId());


        // TODO. 둘의 차이가 뭘까?
        assertEquals(savedGoldUser.getLevel(), GOLD);
        assertThat(savedSilverUser.getLevel()).isEqualTo(SILVER);
    }

    @Test
    public void upgradeAllOrNothing() throws SQLException {
        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);        // TestUserService가 static 클래스이므로 수동 DI를 해준다.
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/toby", "sa", "");
        userDao.deleteAll(connection);
        for (User user : users) {
            userDao.add(connection, user);
        }

        try {
            testUserService.upgradeLevels();
            fail("testUserService.upgradeLevels() 실행 중에 오류가 발생하지 않음");
        } catch (TestUserServiceException e) {
            log.info("TestUserServiceException 오류 발생");
        } catch (SQLException e) {
            log.info("SQLException 오류 발생");
            throw new RuntimeException(e);
        }
        checkLevel(users.get(1), BASIC);        // 💥 도중에 오류가 발생했는데도 롤백되지 않음 (변경 사항이 db에 반영됨)
    }

    // 테스트를 위한 클래스들
    static class TestUserService extends UserService {
        private String id;

        private TestUserService(String id) {
            this.id = id;
        }

        @Override
        protected void upgradeLevel(Connection connection, User user) throws SQLException {
            if (user.getId().equals(id)) {
                throw new TestUserServiceException();
            }
            super.upgradeLevel(connection, user);
        }
    }

    static class TestUserServiceException extends RuntimeException {

    }
}


