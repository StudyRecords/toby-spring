package org.spring.ch6.transaction.userService;

import org.spring.ch6.transaction.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserService {
    void upgradeLevels();

    void add(User user);

    @Transactional(readOnly = true)
    User getById(String id);

    @Transactional(readOnly = true)
    List<User> getAll();

    void deleteAll();

    @Transactional(readOnly = true)
    int getCount();

    void update(User user);
}
