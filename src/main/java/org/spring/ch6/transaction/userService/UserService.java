package org.spring.ch6.transaction.userService;

import org.spring.ch6.transaction.User;

import java.util.List;

public interface UserService {
    void upgradeLevels();

    void add(User user);

    User getById(String id);

    List<User> getAll();

    void deleteAll();

    int getCount();

    void update(User user);
}
