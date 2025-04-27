package org.spring.ch6.proxyFactoryBean;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.spring.ch6.transaction.Level;
import org.spring.ch6.transaction.TestConfig;
import org.spring.ch6.transaction.User;
import org.spring.ch6.transaction.UserDao;
import org.spring.ch6.transaction.userService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.spring.ch6.transaction.Level.*;
import static org.spring.ch6.transaction.UserServiceTest.TestUserServiceException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)          // 각 테스트 메서드간 테스트 인스턴스 공유
public class ProxyFactoryBeanTest {
    //    @Autowired
//    private UserService userService;            // applicationContext.getBean("&userService") 의 반환값은 ProxyFactoryBean 객체이다.
    @Autowired
    private UserService testUserService;
    private List<User> users;
    @Autowired
    private UserDao userDao;

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
    @DirtiesContext
    public void upgradeAllOrNothing() throws Exception {
        // when
        for (User user : users) {
            userDao.add(user);
        }

        // then
        assertThrows(TestUserServiceException.class, testUserService::upgradeLevels);
//        System.out.println(users.get(0).getLevel());
//        System.out.println(users.get(1).getLevel());
//        System.out.println(users.get(2).getLevel());
//        System.out.println(users.get(3).getLevel());
//        System.out.println(users.get(4).getLevel());
        checkLevel(users.get(1), BASIC);
    }


    private void checkLevel(User user, Level level) {
        User updatedUser = userDao.getById(user.getId());
        assertThat(updatedUser.getLevel()).isEqualTo(level);
    }
}
