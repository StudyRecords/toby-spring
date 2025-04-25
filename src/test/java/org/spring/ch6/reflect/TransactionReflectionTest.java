package org.spring.ch6.reflect;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.spring.ch6.transaction.*;
import org.spring.ch6.transaction.userService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.spring.ch6.transaction.Level.*;
import static org.spring.ch6.transaction.UserServiceTest.*;

@ExtendWith(SpringExtension.class)                         // JUnit 5에서 Spring 테스트 확장 활성화
@ContextConfiguration(classes = TestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)            // 각 테스트 메서드간 테스트 인스턴스 공유
public class TransactionReflectionTest {

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
    public void upgradeAllOrNothing() throws Exception {
        // given
        TestUserService testUserService = new TestUserService(users.get(3).getId(), userDao, transactionManager, mailSender);
        TransactionHandler transactionHandler = new TransactionHandler(testUserService, transactionManager, "upgradeLevel");
        UserService txUserService = (UserService) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{UserService.class},
                transactionHandler
        );

        // when
        for (User user : users) {
            userDao.add(user);
        }

        // then
        assertThrows(TestUserServiceException.class, txUserService::upgradeLevels);
        checkLevel(users.get(1), BASIC);
    }


    private void checkLevel(User user, Level level) {
        User updatedUser = userDao.getById(user.getId());
        assertThat(updatedUser.getLevel()).isEqualTo(level);
    }
}
