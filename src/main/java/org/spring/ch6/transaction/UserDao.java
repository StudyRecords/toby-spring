package org.spring.ch6.transaction;

import java.util.List;

public interface UserDao {
    void add(User user);

    User getById(String id);

    List<User> getAll();

    void deleteAll();

    int getCount();

    void update(User user);
}
