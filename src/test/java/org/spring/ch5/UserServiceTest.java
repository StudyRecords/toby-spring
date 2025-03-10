package org.spring.ch5;

import org.junit.jupiter.api.*;
import org.spring.ch5.v1.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.spring.ch5.v1.Level.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)            // 각 테스트 메서드간 테스트 인스턴스 공유
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)      // 테스트 실행 순서 지정 가능
@Transactional               // 테스트 후 자동 롤백
public class UserServiceTest {

    private UserDao userDao;
    private UserService userService;
    private List<User> users;

    @BeforeAll
    void setUp() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        this.userDao = ac.getBean("userDao", UserDao.class);
        this.userService = ac.getBean("userService", UserService.class);
        userDao.deleteAll();

        users = Arrays.asList(new User("user1", "영선", "pass123", BASIC, 0, 30),
                new User("user2", "서니", "pass010", BASIC, 49, 30),
                new User("user3", "선영", "pass323", BASIC, 50, 29),
                new User("user4", "이영선", "pass121", BASIC, 50, 40),
                new User("user5", "이영", "pass212", SILVER, 51, 30));
    }

    @Test
    @Order(1)
    public void bean() {
        assertThat(userDao).isNotNull();
        assertThat(userService).isNotNull();
    }

    @Test
    @Order(2)
    public void upgradeLevels() {
        userDao.deleteAll();
        users.forEach(user -> userDao.add(user));
        userService.upgradeLevels();

        checkLevel(users.get(0), BASIC);
        checkLevel(users.get(1), BASIC);
        checkLevel(users.get(2), SILVER);
        checkLevel(users.get(3), GOLD);
        checkLevel(users.get(4), GOLD);
    }

    private void checkLevel(User user, Level level) {
        User updatedUser = userDao.getById(user.getId());
        assertThat(updatedUser.getLevel()).isEqualTo(level);
    }
}
