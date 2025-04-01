package org.spring.ch5.v3;

import org.junit.jupiter.api.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.spring.ch5.v3.LevelV3.*;
import static org.spring.ch5.v3.UserServiceV3.MIN_LOGIN_FOR_SILVER;
import static org.spring.ch5.v3.UserServiceV3.MIN_RECOMMEND_FOR_GOLD;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)            // 각 테스트 메서드간 테스트 인스턴스 공유
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)      // 테스트 실행 순서 지정 가능
@Transactional               // 테스트 후 자동 롤백
public class UserServiceImplV3Test {

    private UserDaoV3 userDao;
    private UserServiceV3 userService;
    private List<UserV3> users;

    @BeforeEach
    void init() {
        userDao.deleteAll();
    }

    @BeforeAll
    void setUp() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfigV3.class);
        this.userDao = ac.getBean("userDao", UserDaoV3.class);
        this.userService = ac.getBean("userService", UserServiceV3.class);
        userDao.deleteAll();

        users = Arrays.asList(new UserV3("user1", "영선", "pass123", BASIC, MIN_LOGIN_FOR_SILVER - 1, 30),
                new UserV3("user2", "서니", "pass010", BASIC, MIN_LOGIN_FOR_SILVER, 0),
                new UserV3("user3", "선영", "pass323", SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
                new UserV3("user4", "이영선", "pass121", SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new UserV3("user5", "이영", "pass212", GOLD, 100, Integer.MAX_VALUE));
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

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    private void checkLevelUpgraded(UserV3 user, boolean upgraded) {
        LevelV3 nowLevel = user.getLevel();
        if (upgraded) {
            user.upgradeLevel();
            assertThat(user.getLevel()).isEqualTo(nowLevel.nextLevel());
        } else {
            assertThat(user.getLevel()).isEqualTo(nowLevel);
        }
    }

    @Test
    @Order(3)
    public void add() {
        UserV3 goldUser = users.get(4);
        UserV3 silverUser = users.get(2);

        userService.add(goldUser);
        userService.add(silverUser);

        UserV3 savedGoldUser = userDao.getById(goldUser.getId());
        UserV3 savedSilverUser = userDao.getById(silverUser.getId());

        assertThat(savedGoldUser.getLevel()).isEqualTo(GOLD);
        assertThat(savedSilverUser.getLevel()).isEqualTo(SILVER);
    }
}
