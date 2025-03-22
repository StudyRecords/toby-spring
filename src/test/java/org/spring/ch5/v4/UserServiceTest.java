package org.spring.ch5.v4;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.spring.ch5.v4.LevelV4.*;

@ExtendWith(SpringExtension.class)  // JUnit 5에서 Spring 테스트 확장 활성화
@ContextConfiguration(classes = AppConfigV4.class)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)            // 각 테스트 메서드간 테스트 인스턴스 공유
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)      // 테스트 실행 순서 지정 가능
@Transactional                                             // 테스트 후 자동 롤백
public class UserServiceTest {

//    private UserDao userDao;
//    private UserService userService;
    private List<UserV4> users;


    @Autowired private UserDaoV4 userDao;
    @Autowired private UserServiceV4 userService;

    @BeforeEach
    void init() {
        userDao.deleteAll();
    }

    @BeforeAll
    void setUp() {
//        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
//        this.userDao = ac.getBean("userDao", UserDao.class);
//        this.userService = ac.getBean("userService", UserService.class);
        userDao.deleteAll();

        users = Arrays.asList(new UserV4("user1", "영선", "pass123", BASIC, 0, 30),
                new UserV4("user2", "서니", "pass010", BASIC, 49, 30),
                new UserV4("user3", "선영", "pass323", SILVER, 50, 29),
                new UserV4("user4", "이영선", "pass121", SILVER, 50, 40),
                new UserV4("user5", "이영", "pass212", GOLD, 51, 30));
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
        checkLevel(users.get(1), BASIC);
        checkLevel(users.get(2), SILVER);
        checkLevel(users.get(3), GOLD);
        checkLevel(users.get(4), GOLD);
    }

    private void checkLevel(UserV4 user, LevelV4 level) {
        UserV4 updatedUser = userDao.getById(user.getId());
        assertThat(updatedUser.getLevel()).isEqualTo(level);
    }

    @Test
    @Order(3)
    public void add() {
        UserV4 goldUser = users.get(4);
        UserV4 silverUser = users.get(2);

        userService.add(goldUser);
        userService.add(silverUser);

        UserV4 savedGoldUser = userDao.getById(goldUser.getId());
        UserV4 savedSilverUser = userDao.getById(silverUser.getId());


        // TODO. 둘의 차이가 뭘까?
        assertEquals(savedGoldUser.getLevel(), GOLD);
        assertThat(savedSilverUser.getLevel()).isEqualTo(SILVER);
    }
}
