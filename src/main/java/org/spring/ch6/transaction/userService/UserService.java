package org.spring.ch6.transaction.userService;

import org.spring.ch6.transaction.User;

public interface UserService {
    void upgradeLevels();
    void add(User user);
}
