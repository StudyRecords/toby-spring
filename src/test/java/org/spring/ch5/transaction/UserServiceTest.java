package org.spring.ch5.transaction;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.spring.ch5.transaction.Level.*;

@ExtendWith(SpringExtension.class)                         // JUnit 5에서 Spring 테스트 확장 활성화
@ContextConfiguration(classes = AppConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)            // 각 테스트 메서드간 테스트 인스턴스 공유
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)      // 테스트 실행 순서 지정 가능
//@Transactional                                             // 테스트 후 자동 롤백
public class UserServiceTest {

    private List<User> users;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private MailSender mailSender;

    @BeforeEach
    void init() {
        userDao.deleteAll();
        users = Arrays.asList(
                new User("user1", "AAA", "passAAA", BASIC, 0, 30, "aaa@gmail.com"),
                new User("user2", "BBB", "passBBB", BASIC, 50, 20, "bbb@gmail.com"),
                new User("user3", "CCC", "passCCC", SILVER, 51, 29, "ccc@gmail.com"),
                new User("user4", "DDD", "passDDD", SILVER, 50, 40, "ddd@gmail.com"),
                new User("user5", "EEE", "passEEE", GOLD, 51, 30, "eee@gmail.com")
        );
    }

    @Test
    @Order(1)
    public void bean() {
        assertThat(userDao).isNotNull();
        assertThat(userService).isNotNull();
    }

    @Test
    public void upgradeLevels() {
        users.forEach(user -> userDao.add(user));
        userService.upgradeLevels();

        checkLevel(users.get(0), BASIC);
        checkLevel(users.get(1), SILVER);       // update 1
        checkLevel(users.get(2), SILVER);
        checkLevel(users.get(3), GOLD);         // update 2
        checkLevel(users.get(4), GOLD);
    }

    private void checkLevel(User user, Level level) {
        User updatedUser = userDao.getById(user.getId());
        assertThat(updatedUser.getLevel()).isEqualTo(level);
    }

    @Test
    @Order(3)
    public void add() {
        User goldUser = users.get(4);
        User silverUser = users.get(2);

        userService.add(goldUser);
        userService.add(silverUser);

        User savedGoldUser = userDao.getById(goldUser.getId());
        User savedSilverUser = userDao.getById(silverUser.getId());


        // TODO. 둘의 차이가 뭘까?
        assertEquals(savedGoldUser.getLevel(), GOLD);
        assertThat(savedSilverUser.getLevel()).isEqualTo(SILVER);
    }

    @Test
    public void upgradeAllOrNothing() {
        TestUserService testUserService = new TestUserService(users.get(3).getId(), userDao, transactionManager, mailSender);
//        testUserService.setUserDao(userDao);        // TestUserService가 static 클래스이므로 수동 DI를 해준다.
//        testUserService.setDataSource(dataSource);

        for (User user : users) {
            userDao.add(user);
        }


        assertThrows(TestUserServiceException.class, testUserService::upgradeLevels);

        checkLevel(users.get(1), BASIC);        // 💥 도중에 오류가 발생했는데도 롤백되지 않음 (변경 사항이 db에 반영됨)
    }

    // 테스트를 위한 클래스들
    static class TestUserService extends UserService {
        private String id;

        private TestUserService(String id,
                                UserDao userDao,
                                PlatformTransactionManager transactionManager,
                                MailSender mailSender) {
            super(userDao, transactionManager, mailSender);
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(id)) {
                throw new TestUserServiceException();
            }
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {

    }
}


