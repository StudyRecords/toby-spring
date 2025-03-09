package org.spring.ch5.v1;

import org.spring.ch4.User;

import java.util.List;

public interface UserDao {
    void add(User user);

    User getById(String id);

    List<User> getAll();

    void deleteAll();

    int getCount();
}
