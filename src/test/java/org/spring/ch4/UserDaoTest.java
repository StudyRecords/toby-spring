package org.spring.ch4;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.spring.User;
import org.spring.ch4.independentDao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
@Transactional // 테스트 후 자동 롤백
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 삽입
        jdbcTemplate.execute("INSERT INTO users (id, name, password) VALUES ('user1', 'Alice', 'pass123')");
        jdbcTemplate.execute("INSERT INTO users (id, name, password) VALUES ('user2', 'Bob', 'pass456')");
    }

    @Test
    void testGetById_Found() {
        User user = userDao.getById("user1");
        Assertions.assertThat(user.getName()).isEqualTo("Alice");
    }

    @Test
    void testGetById_NotFound() {
        User user = userDao.getById("nonexistent");
        assertThat(user).isEmpty();
    }

    @Test
    void testGetAll() {
        List<User> users = userDao.getAll();
        assertThat(users).hasSize(2);
    }

    @Test
    void testGetCount() {
        int count = userDao.getCount();
        assertThat(count).isEqualTo(2);
    }

    @Test
    void testSave() {
        User newUser = new User("user3", "Charlie", "pass789");
        userDao.save(newUser);

        Optional<User> savedUser = userDao.getById("user3");
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getName()).isEqualTo("Charlie");
    }

    @Test
    void testDelete() {
        userDao.delete("user1");
        Optional<User> user = userDao.getById("user1");
        assertThat(user).isEmpty();
    }
}

