package org.spring.ch6.transaction;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.spring.ch6.transaction.userService.UserService;
import org.spring.ch6.transaction.userService.UserServiceImpl;
import org.spring.ch6.transaction.userService.UserServiceTx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.spring.ch6.transaction.Level.*;


@ExtendWith(SpringExtension.class)                         // JUnit 5에서 Spring 테스트 확장 활성화
@ContextConfiguration(classes = TestConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)            // 각 테스트 메서드간 테스트 인스턴스 공유
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)      // 테스트 실행 순서 지정 가능
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
    @DirtiesContext         // 컨텍스트의 DI 설정을 변경한 테스트라는 것을 명시
    public void upgradeLevels() {
        // given
        UserDao mockUserDao = Mockito.mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
        MailSender mockMailSender = Mockito.mock(MailSender.class);        //  메일 발송 여부 확인을 위한 목 오브젝트 수동 DI
        UserService userService = new UserServiceTx(transactionManager, new UserServiceImpl(mockUserDao, mockMailSender));

        // when
        userService.upgradeLevels();

        // then - update
        // 1️⃣ 호출 횟수 확인
        verify(mockUserDao, times(2)).update(any(User.class));

        // 2️⃣ 목 오브젝트 호출 시 파라미터를 하나씩 점검
        verify(mockUserDao).update(users.get(1));
        verify(mockUserDao).update(users.get(3));

        // 3️⃣ 각 User의 레벨 변경을 직접 확인
        assertThat(users.get(1).getLevel()).isEqualTo(SILVER);
        assertThat(users.get(3).getLevel()).isEqualTo(GOLD);

        // then - mail
        // ArgumentCaptor를 사용해서 실제 MailSender 목 오브젝트에 전달된 파라미터를 가져옴 (파라미터의 내부 정보 가져오기)
        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();

        assertThat(mailMessages.get(0).getTo()[0]).isEqualTo(users.get(1).getEmail());
        assertThat(mailMessages.get(1).getTo()[0]).isEqualTo(users.get(3).getEmail());
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
        UserServiceTx userServiceTx = new UserServiceTx(transactionManager, testUserService);

        for (User user : users) {
            userDao.add(user);
        }

        assertThrows(TestUserServiceException.class, userServiceTx::upgradeLevels);

        checkLevel(users.get(1), BASIC);        // 💥 도중에 오류가 발생했는데도 롤백되지 않음 (변경 사항이 db에 반영됨)
    }

    // 테스트를 위한 클래스들
    public static class TestUserService extends UserServiceImpl {
        private String id;

        public TestUserService(String id,
                               UserDao userDao,
                               PlatformTransactionManager transactionManager,
                               MailSender mailSender) {
            super(userDao, mailSender);
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

    public static class TestUserServiceException extends RuntimeException {

    }
}


