package org.spring.ch5.v1;

import java.util.List;

public interface UserDao {
    void add(User user);

    User getById(String id);

    List<User> getAll();

    void deleteAll();

    int getCount();
}
